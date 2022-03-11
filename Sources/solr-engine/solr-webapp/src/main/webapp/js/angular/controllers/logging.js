/*
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

var format_time_content = function( time, timeZone ) {
    var format_time_options = {};
    if (timeZone && timeZone!="Local") {
        format_time_options.timeZone = timeZone;
    }
    return time.toLocaleString( undefined, format_time_options );
}

var server_time = function() {
    return new Date().getTime() + window.timeDiff;
}

solrAdminApp.controller('LoggingController',
    function($scope, $timeout, $cookies, Logging, Constants, Cores){
        $scope.resetMenu("logging", Constants.IS_ROOT_PAGE);
        $scope.timezone = $cookies.logging_timezone || "Local";

        $scope.clear = function() {
            $scope.since = server_time();
            $scope.refresh();
        }

        $scope.refresh = function() {

            Logging.events( {since: $scope.since } , function(data) {
                //$scope.since = new Date();
                $scope.lastDisplay = format_time_content( new Date(server_time()), $scope.timezone);
                $scope.sinceDisplay = format_time_content( new Date($scope.since) , $scope.timezone);
                var events = data.history.docs;

                if ($scope.inverted)
                {
                    events = events.reverse();
                }

                for (var i=0; i<events.length; i++) {

                    var event = events[i];
                    var time = new Date(event.time);
                    event.local_time = format_time_content(time, "Local");
                    event.utc_time = format_time_content(time, "UTC");
                    event.loggerBase = event.logger.split( '.' ).pop();

                    if( !event.trace ) {
                        var lines = event.message.split( "\n" );
                        if( lines.length > 1) {
                            event.trace = event.message;
                            event.message = lines[0];
                        }
                    }
                    event.message = event.message.replace(/,/g, ',&#8203;');
                    event.showTrace = false;
                }
                $scope.events = events;
                $scope.watcher = data.watcher;
                /* @todo sticky_mode
                // state element is in viewport
                sticky_mode = ( state.position().top <= $( window ).scrollTop() + $( window ).height() - ( $( 'body' ).height() - state.position().top ) );
                // initial request
                if( 0 === since ) {
                  sticky_mode = true;
                }
                $scope.loggingEvents = events;

                if( sticky_mode )
                {
                  $( 'body' )
                    .animate
                    (
                        { scrollTop: state.position().top },
                        1000
                    );
                }
              */
            });

            $timeout.cancel($scope.timeout);
            if (!$scope.stopped)
                $scope.timeout = $timeout($scope.refresh, 10000);

            var onRouteChangeOff = $scope.$on('$routeChangeStart', function() {
                $timeout.cancel($scope.timeout);
                onRouteChangeOff();
            });
        };


        //parte stoppato e con gli eventi nuovi
        $scope.stopped = true;

        if (!window.timeDiff){
            Cores.list(function(data) {
                for (key in data.status) {
                    var core = data.status[key];
                    var serverTime = new Date(Date.parse(core.startTime) + core.uptime);
                    window.timeDiff = serverTime - new Date().getTime() ;
                    break;
                }

                $scope.since = server_time();

                console.log(new Date());
                console.log(new Date($scope.since));

                $scope.refresh();
            });
        } else {
            $scope.since = server_time();
            $scope.refresh();
        }

        $scope.timezone = "Local";
        $cookies.logging_timezone = $scope.timezone;

        $scope.toggleRefresh = function() {
            if(!$scope.stopped) {
                $scope.stopped = true;
                $timeout.cancel($scope.timeout);
            } else {
                $scope.stopped = false;
                $scope.refresh();
            }
        };
        $scope.toggleTimezone = function() {
            $scope.timezone = ($scope.timezone=="Local") ? "UTC":"Local";
            $cookies.logging_timezone = $scope.timezone;
            $scope.refresh();
        }
        $scope.toggleSince = function() {

            if ($scope.since==0){
                $scope.since = server_time();
                $scope.refresh();
            } else {
                $scope.since = 0;
                $scope.refresh();
            }
        }
        $scope.toggleRow = function(event) {
            event.showTrace =! event.showTrace;
        };

        $scope.toggleInverted = function(event) {
            $scope.inverted =! $scope.inverted;
            $scope.refresh();
        };
    }
)

    .controller('LoggingLevelController',
        function($scope, Logging) {
            $scope.resetMenu("logging-levels");

            var packageOf = function(logger) {
                var parts = logger.name.split(".");
                return !parts.pop() ? "" : parts.join(".");
            };

            var shortNameOf = function(logger) {return logger.name.split(".").pop();}

            var makeTree = function(loggers, packag) {
                var tree = [];

                if (packag==""){
                    var threshold = {};
                    threshold.packag = null;
                    threshold.level = $scope.threshold;
                    threshold.short = "UI Threshold level";
                    threshold.name = "threshold";
                    threshold.set = true;

                    tree.push(threshold);
                }
                for (var i=0; i<loggers.length; i++) {
                    var logger = loggers[i];
                    logger.packag = packageOf(logger);
                    logger.short = shortNameOf(logger);
                    if (logger.packag == packag) {
                        logger.children = makeTree(loggers, logger.name);
                        tree.push(logger);
                    }
                }
                return tree;
            };

            $scope.refresh = function() {

                Logging.events( {since: new Date().getTime()+1000 } , function(data) {

                    $scope.threshold = data.info.threshold;

                    Logging.levels(function(data) {
                        $scope.logging = makeTree(data.loggers, "");

                        $scope.watcher = data.watcher;
                        $scope.levels = [];
                        for (level in data.levels) {
                            $scope.levels.push({name:data.levels[level], pos:level});
                        }
                    });

                });


            };

            $scope.toggleOptions = function(logger) {
                if (logger.showOptions) {
                    logger.showOptions = false;
                    delete $scope.currentLogger;
                } else {
                    if ($scope.currentLogger) {
                        $scope.currentLogger.showOptions = false;
                    }
                    logger.showOptions = true;
                    $scope.currentLogger = logger;
                }
            };

            $scope.setLevel = function(logger, newLevel) {

                var setString = logger.name + ":" + newLevel;
                logger.showOptions = false;
                var params = {set: setString};
                if (logger.name == "threshold"){
                    params = {threshold : newLevel};
                }
                Logging.setLevel(params, function(data) {
                    $scope.refresh();
                });
            };

            $scope.refresh();
        });
