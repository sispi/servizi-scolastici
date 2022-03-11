var global = this;
var module = this;
var exports = this;

'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

// name: nashorn-async
// license: MIT (http://opensource.org/licenses/MIT)
// authors: Nico Rehwaldt <git_nikku@nixis.de>
// homepage: https://github.com/nikku/nashorn-async#readme
// version: 4.0.0
// import src/main/resources/js/event-loop.js
/**
 * Execution support for simple hybrid (async/sync) script execution in nashorn.
 *
 * Extend your nashorn script engine with async support by evaluating this script prior to the actual script.
 * Then, wrap the actual script you would like to execute in a `exec(fn, timeout)` block with `fn` containing
 * it.
 *
 * Scripts may call {@link #async} to retrieve a callback (err, result) that needs to be
 * triggered before the overall script execution terminates.
 *
 * <strong>Warning: </strong> If {@link #async} is invoked multiple times, all callbacks
 * need to be fulfilled but only the last one will decide upon the actual execution result.
 *
 * The function will always return synchronously and indicate script errors by throwing them.
 *
 * @example async
 *
 * exec(function() {
 *   var done = async();
 *
 *   setTimeout(function() {
 *     done(null, 'YES IT WORKED');
 *   }, 200);
 * });
 *
 * @example sync
 *
 * exec(function() {
 *   throw new Error('FAILED');
 * });
 *
 *
 * @author Nico Rehwaldt<git_nikku@nixis.de>
 */

function initNahorn(context) {

    var Timer = Java.type('java.util.Timer');
    var TimerTask = Java.type('java.util.TimerTask');
    var Phaser = Java.type('java.util.concurrent.Phaser');
    var TimeUnit = Java.type('java.util.concurrent.TimeUnit');
    var Executors = Java.type('java.util.concurrent.Executors');

    var System = Java.type('java.lang.System');

    var timer = new Timer('jsEventLoop', false);
    //var scheduler = Executors.newScheduledThreadPool(10)

    /**
     * Our global synchronization barrier that we use to register async operations
     * and that awaits for all of these operations to finish.
     */
    var phaser = new Phaser();


    /** global execution results */
    var results;

    function eventLoopError(e) {
        System.out.print('Exception in jsEventLoop ');
        e.printStackTrace();
    }

    function async() {
        phaser.register();

        return function(err, result) {
            results = { err: err, result: result };
            console.log(result);
            phaser.arriveAndDeregister();
        };
    }


    /**
     * Executes the given function after the specified timeout.
     *
     * @param {Function} fn
     * @param {Number} [millis]
     *
     * @return {Object} timeout handle
     */
    function setTimeout(fn, millis) {
        var args = [].slice.call(arguments, 2, arguments.length);
        phaser.register();

        var task = new TimerTask({
            run: function() {
                try {
                    fn.apply(this, args);
                } catch(e) {
                    eventLoopError(e);
                } finally {
                    phaser.arriveAndDeregister();
                }
            }
        });

        timer.schedule(task, millis || 0);

        return task;
    }


    /**
     * Clear timeout previously created via {@link #setTimeout}.
     *
     * @param {Object} task timeout handle
     */
    function clearTimeout(task) {
        if (task && task.cancel()) {
            phaser.arriveAndDeregister();
        }
    }

    /**
     * Executes the given function with a fixed time delay.
     *
     * @param {Function} fn
     * @param {Number} [millis]
     *
     * @return {Object} timeout handle
     */
    function setInterval(fn, millis) {
        phaser.register();

        var task = new TimerTask({
            run: function() {
                try {
                    fn();
                } catch (e) {
                    eventLoopError(e);
                }
            }
        });

        timer.scheduleAtFixedRate(task, millis, millis);

        return task;
    }


    /**
     * Clear interval previously created via {@link #setInterval}.
     *
     * @param {Object} interval handle
     */
    function clearInterval(task) {
        if (task.cancel()) {
            phaser.arriveAndDeregister();
        }
    }

    /**
     * Execute a function (synchronously or asynchronously) and return the result
     * or throw the error that is returned by it.
     *
     * @param {Function} fn
     * @param {Number} [timeout=5000]
     */
    function exec(fn, timeout) {
        timeout = timeout || 5000;

        phaser.register();

        setTimeout(function() {
            try {
                results = { err: null, result: fn() };
            } catch (e) {
                results = { err: e };
            }
        }, 0);

        try {
            if (timeout < 0) {
                phaser.awaitAdvanceInterruptibly(phaser.arrive());
            } else {
                phaser.awaitAdvanceInterruptibly(phaser.arrive(), timeout, TimeUnit.MILLISECONDS);
            }
        } catch (e) {
            results = { err: new Error('execution timeout') };
        } finally {
            timer.cancel();
        }

        if (results.err) {
            throw results.err;
        } else {
            return results.result;
        }
    }

    context.exec = exec;

    context.async = async;

    context.setTimeout = setTimeout;
    context.clearTimeout = clearTimeout;
    context.setInterval = setInterval;
    context.clearInterval = clearInterval;

}

// shim for using process in browser
// based off https://github.com/defunctzombie/node-process/blob/master/browser.js

function defaultSetTimout() {
    throw new Error('setTimeout has not been defined');
}
function defaultClearTimeout () {
    throw new Error('clearTimeout has not been defined');
}
var cachedSetTimeout = defaultSetTimout;
var cachedClearTimeout = defaultClearTimeout;
if (typeof global.setTimeout === 'function') {
    cachedSetTimeout = setTimeout;
}
if (typeof global.clearTimeout === 'function') {
    cachedClearTimeout = clearTimeout;
}

function runTimeout(fun) {
    if (cachedSetTimeout === setTimeout) {
        //normal enviroments in sane situations
        return setTimeout(fun, 0);
    }
    // if setTimeout wasn't available but was latter defined
    if ((cachedSetTimeout === defaultSetTimout || !cachedSetTimeout) && setTimeout) {
        cachedSetTimeout = setTimeout;
        return setTimeout(fun, 0);
    }
    try {
        // when when somebody has screwed with setTimeout but no I.E. maddness
        return cachedSetTimeout(fun, 0);
    } catch(e){
        try {
            // When we are in I.E. but the script has been evaled so I.E. doesn't trust the global object when called normally
            return cachedSetTimeout.call(null, fun, 0);
        } catch(e){
            // same as above but when it's a version of I.E. that must have the global object for 'this', hopfully our context correct otherwise it will throw a global error
            return cachedSetTimeout.call(this, fun, 0);
        }
    }


}
function runClearTimeout(marker) {
    if (cachedClearTimeout === clearTimeout) {
        //normal enviroments in sane situations
        return clearTimeout(marker);
    }
    // if clearTimeout wasn't available but was latter defined
    if ((cachedClearTimeout === defaultClearTimeout || !cachedClearTimeout) && clearTimeout) {
        cachedClearTimeout = clearTimeout;
        return clearTimeout(marker);
    }
    try {
        // when when somebody has screwed with setTimeout but no I.E. maddness
        return cachedClearTimeout(marker);
    } catch (e){
        try {
            // When we are in I.E. but the script has been evaled so I.E. doesn't  trust the global object when called normally
            return cachedClearTimeout.call(null, marker);
        } catch (e){
            // same as above but when it's a version of I.E. that must have the global object for 'this', hopfully our context correct otherwise it will throw a global error.
            // Some versions of I.E. have different rules for clearTimeout vs setTimeout
            return cachedClearTimeout.call(this, marker);
        }
    }



}
var queue = [];
var draining = false;
var currentQueue;
var queueIndex = -1;

function cleanUpNextTick() {
    if (!draining || !currentQueue) {
        return;
    }
    draining = false;
    if (currentQueue.length) {
        queue = currentQueue.concat(queue);
    } else {
        queueIndex = -1;
    }
    if (queue.length) {
        drainQueue();
    }
}

function drainQueue() {
    if (draining) {
        return;
    }
    var timeout = runTimeout(cleanUpNextTick);
    draining = true;

    var len = queue.length;
    while(len) {
        currentQueue = queue;
        queue = [];
        while (++queueIndex < len) {
            if (currentQueue) {
                currentQueue[queueIndex].run();
            }
        }
        queueIndex = -1;
        len = queue.length;
    }
    currentQueue = null;
    draining = false;
    runClearTimeout(timeout);
}
function nextTick(fun) {
    var arguments$1 = arguments;

    var args = new Array(arguments.length - 1);
    if (arguments.length > 1) {
        for (var i = 1; i < arguments.length; i++) {
            args[i - 1] = arguments$1[i];
        }
    }
    queue.push(new Item(fun, args));
    if (queue.length === 1 && !draining) {
        runTimeout(drainQueue);
    }
}
// v8 likes predictible objects
function Item(fun, array) {
    this.fun = fun;
    this.array = array;
}
Item.prototype.run = function () {
    this.fun.apply(null, this.array);
};
var title = 'browser';
var platform = 'browser';
var browser = true;
var env = {};
var argv = [];
var version = ''; // empty string to avoid regexp issues
var versions = {};
var release = {};
var config = {};

function noop() {}

var on = noop;
var addListener = noop;
var once = noop;
var off = noop;
var removeListener = noop;
var removeAllListeners = noop;
var emit = noop;

function binding(name) {
    throw new Error('process.binding is not supported');
}

function cwd () { return '/' }
function chdir (dir) {
    throw new Error('process.chdir is not supported');
}
function umask() { return 0; }

// from https://github.com/kumavis/browser-process-hrtime/blob/master/index.js
var performance = global.performance || {};
var performanceNow =
    performance.now        ||
    performance.mozNow     ||
    performance.msNow      ||
    performance.oNow       ||
    performance.webkitNow  ||
    function(){ return (new Date()).getTime() };

// generate timestamp or delta
// see http://nodejs.org/api/process.html#process_process_hrtime
function hrtime(previousTimestamp){
    var clocktime = performanceNow.call(performance)*1e-3;
    var seconds = Math.floor(clocktime);
    var nanoseconds = Math.floor((clocktime%1)*1e9);
    if (previousTimestamp) {
        seconds = seconds - previousTimestamp[0];
        nanoseconds = nanoseconds - previousTimestamp[1];
        if (nanoseconds<0) {
            seconds--;
            nanoseconds += 1e9;
        }
    }
    return [seconds,nanoseconds]
}

var startTime = new Date();
function uptime() {
    var currentTime = new Date();
    var dif = currentTime - startTime;
    return dif / 1000;
}

var process$1 = {
    nextTick: nextTick,
    title: title,
    browser: browser,
    env: env,
    argv: argv,
    version: version,
    versions: versions,
    on: on,
    addListener: addListener,
    once: once,
    off: off,
    removeListener: removeListener,
    removeAllListeners: removeAllListeners,
    emit: emit,
    binding: binding,
    cwd: cwd,
    chdir: chdir,
    umask: umask,
    hrtime: hrtime,
    platform: platform,
    release: release,
    config: config,
    uptime: uptime
};

global.console = {
    debug: print,
    error: print,
    warn: print,
    log: print,
    trace: print,
    assert: print
};

global.process = process$1;

initNahorn(global);

Object.assign = function (target, varArgs) {
    var arguments$1 = arguments;
    // .length of function is 2

    if (target == null) { // TypeError if undefined or null
        throw new TypeError('Cannot convert undefined or null to object')
    }

    var to = Object(target);

    for (var index = 1; index < arguments.length; index++) {
        var nextSource = arguments$1[index];

        if (nextSource != null) { // Skip over if undefined or null
            for (var nextKey in nextSource) {
                // Avoid bugs when hasOwnProperty is shadowed
                if (Object.prototype.hasOwnProperty.call(nextSource, nextKey)) {
                    to[nextKey] = nextSource[nextKey];
                }
            }
        }
    }
    return to
};

Object.defineProperty(Array.prototype, 'find', {
    value: function (predicate) {
        // 1. Let O be ? ToObject(this value).
        if (this == null) {
            throw new TypeError('"this" is null or not defined')
        }

        var o = Object(this);

        // 2. Let len be ? ToLength(? Get(O, "length")).
        var len = o.length >>> 0;

        // 3. If IsCallable(predicate) is false, throw a TypeError exception.
        if (typeof predicate !== 'function') {
            throw new TypeError('predicate must be a function')
        }

        // 4. If thisArg was supplied, let T be thisArg; else let T be undefined.
        var thisArg = arguments[1];
        // 5. Let k be 0.
        var k = 0;
        // 6. Repeat, while k < len
        while (k < len) {
            // a. Let Pk be ! ToString(k).
            // b. Let kValue be ? Get(O, Pk).
            // c. Let testResult be ToBoolean(? Call(predicate, T, « kValue, k, O »)).
            // d. If testResult is true, return kValue.
            var kValue = o[k];
            if (predicate.call(thisArg, kValue, k, o)) {
                return kValue
            }
            // e. Increase k by 1.
            k++;
        }
        // 7. Return undefined.
        return undefined
    }
});

var awaitServer = function (fn) {
    var Phaser = Java.type('java.util.concurrent.Phaser');
    var phaser = new Phaser();
    var results = {error: null, result: ''};
    phaser.register();

    var done = function (err, res) {
        if (err) {
            results.error = err;
        } else {
            results.result = res;
        }
        phaser.arriveAndDeregister();
    };

    phaser.register();
    setTimeout(function () {
        fn(done);
    }, 0);
    //nashornEventLoop.process();
    phaser.awaitAdvanceInterruptibly(phaser.arrive());
    return results
};

/*  */

// these helpers produces better vm code in JS engines due to their
// explicitness and function inlining
function isUndef (v) {
    return v === undefined || v === null
}

function isDef (v) {
    return v !== undefined && v !== null
}

function isTrue (v) {
    return v === true
}

/**
 * Check if value is primitive
 */
function isPrimitive (value) {
    return typeof value === 'string' || typeof value === 'number'
}

/**
 * Quick object check - this is primarily used to tell
 * Objects from primitive values when we know the value
 * is a JSON-compliant type.
 */
function isObject (obj) {
    return obj !== null && typeof obj === 'object'
}

var toString = Object.prototype.toString;

/**
 * Strict object type check. Only returns true
 * for plain JavaScript objects.
 */
function isPlainObject (obj) {
    return toString.call(obj) === '[object Object]'
}

function isRegExp (v) {
    return toString.call(v) === '[object RegExp]'
}

/**
 * Convert a value to a string that is actually rendered.
 */
function _toString (val) {
    return val == null
        ? ''
        : typeof val === 'object'
            ? JSON.stringify(val, null, 2)
            : String(val)
}

/**
 * Convert a input value to a number for persistence.
 * If the conversion fails, return original string.
 */
function toNumber (val) {
    var n = parseFloat(val);
    return isNaN(n) ? val : n
}

/**
 * Make a map and return a function for checking if a key
 * is in that map.
 */
function makeMap (
    str,
    expectsLowerCase
) {
    var map = Object.create(null);
    var list = str.split(',');
    for (var i = 0; i < list.length; i++) {
        map[list[i]] = true;
    }
    return expectsLowerCase
        ? function (val) { return map[val.toLowerCase()]; }
        : function (val) { return map[val]; }
}

/**
 * Check if a tag is a built-in tag.
 */
var isBuiltInTag = makeMap('slot,component', true);

/**
 * Remove an item from an array
 */
function remove (arr, item) {
    if (arr.length) {
        var index = arr.indexOf(item);
        if (index > -1) {
            return arr.splice(index, 1)
        }
    }
}

/**
 * Check whether the object has the property.
 */
var hasOwnProperty = Object.prototype.hasOwnProperty;
function hasOwn (obj, key) {
    return hasOwnProperty.call(obj, key)
}

/**
 * Create a cached version of a pure function.
 */
function cached (fn) {
    var cache = Object.create(null);
    return (function cachedFn (str) {
        var hit = cache[str];
        return hit || (cache[str] = fn(str))
    })
}

/**
 * Camelize a hyphen-delimited string.
 */
var camelizeRE = /-(\w)/g;
var camelize = cached(function (str) {
    return str.replace(camelizeRE, function (_, c) { return c ? c.toUpperCase() : ''; })
});

/**
 * Capitalize a string.
 */
var capitalize = cached(function (str) {
    return str.charAt(0).toUpperCase() + str.slice(1)
});

/**
 * Hyphenate a camelCase string.
 */
var hyphenateRE = /([^-])([A-Z])/g;
var hyphenate = cached(function (str) {
    return str
        .replace(hyphenateRE, '$1-$2')
        .replace(hyphenateRE, '$1-$2')
        .toLowerCase()
});

/**
 * Simple bind, faster than native
 */
function bind (fn, ctx) {
    function boundFn (a) {
        var l = arguments.length;
        return l
            ? l > 1
                ? fn.apply(ctx, arguments)
                : fn.call(ctx, a)
            : fn.call(ctx)
    }
    // record original fn length
    boundFn._length = fn.length;
    return boundFn
}

/**
 * Convert an Array-like object to a real Array.
 */
function toArray (list, start) {
    start = start || 0;
    var i = list.length - start;
    var ret = new Array(i);
    while (i--) {
        ret[i] = list[i + start];
    }
    return ret
}

/**
 * Mix properties into target object.
 */
function extend (to, _from) {
    for (var key in _from) {
        to[key] = _from[key];
    }
    return to
}

/**
 * Merge an Array of Objects into a single Object.
 */
function toObject (arr) {
    var res = {};
    for (var i = 0; i < arr.length; i++) {
        if (arr[i]) {
            extend(res, arr[i]);
        }
    }
    return res
}

/**
 * Perform no operation.
 */
function noop$1 () {}

/**
 * Always return false.
 */
var no = function () { return false; };

/**
 * Return same value
 */
var identity = function (_) { return _; };

/**
 * Generate a static keys string from compiler modules.
 */
function genStaticKeys (modules) {
    return modules.reduce(function (keys, m) {
        return keys.concat(m.staticKeys || [])
    }, []).join(',')
}

/**
 * Check if two values are loosely equal - that is,
 * if they are plain objects, do they have the same shape?
 */
function looseEqual (a, b) {
    var isObjectA = isObject(a);
    var isObjectB = isObject(b);
    if (isObjectA && isObjectB) {
        try {
            return JSON.stringify(a) === JSON.stringify(b)
        } catch (e) {
            // possible circular reference
            return a === b
        }
    } else if (!isObjectA && !isObjectB) {
        return String(a) === String(b)
    } else {
        return false
    }
}

function looseIndexOf (arr, val) {
    for (var i = 0; i < arr.length; i++) {
        if (looseEqual(arr[i], val)) { return i }
    }
    return -1
}

/**
 * Ensure a function is called only once.
 */
function once$1 (fn) {
    var called = false;
    return function () {
        if (!called) {
            called = true;
            fn.apply(this, arguments);
        }
    }
}

var SSR_ATTR = 'data-server-rendered';

var ASSET_TYPES = [
    'component',
    'directive',
    'filter'
];

var LIFECYCLE_HOOKS = [
    'beforeCreate',
    'created',
    'beforeMount',
    'mounted',
    'beforeUpdate',
    'updated',
    'beforeDestroy',
    'destroyed',
    'activated',
    'deactivated'
];

/*  */

var config$1 = ({
    /**
     * Option merge strategies (used in core/util/options)
     */
    optionMergeStrategies: Object.create(null),

    /**
     * Whether to suppress warnings.
     */
    silent: false,

    /**
     * Show production mode tip message on boot?
     */
    productionTip: "production" !== 'production',

    /**
     * Whether to enable devtools
     */
    devtools: "production" !== 'production',

    /**
     * Whether to record perf
     */
    performance: false,

    /**
     * Error handler for watcher errors
     */
    errorHandler: null,

    /**
     * Ignore certain custom elements
     */
    ignoredElements: [],

    /**
     * Custom user key aliases for v-on
     */
    keyCodes: Object.create(null),

    /**
     * Check if a tag is reserved so that it cannot be registered as a
     * component. This is platform-dependent and may be overwritten.
     */
    isReservedTag: no,

    /**
     * Check if an attribute is reserved so that it cannot be used as a component
     * prop. This is platform-dependent and may be overwritten.
     */
    isReservedAttr: no,

    /**
     * Check if a tag is an unknown element.
     * Platform-dependent.
     */
    isUnknownElement: no,

    /**
     * Get the namespace of an element
     */
    getTagNamespace: noop$1,

    /**
     * Parse the real tag name for the specific platform.
     */
    parsePlatformTagName: identity,

    /**
     * Check if an attribute must be bound using property, e.g. value
     * Platform-dependent.
     */
    mustUseProp: no,

    /**
     * Exposed for legacy reasons
     */
    _lifecycleHooks: LIFECYCLE_HOOKS
});

/*  */

var emptyObject = Object.freeze({});

/**
 * Check if a string starts with $ or _
 */
function isReserved (str) {
    var c = (str + '').charCodeAt(0);
    return c === 0x24 || c === 0x5F
}

/**
 * Define a property.
 */
function def (obj, key, val, enumerable) {
    Object.defineProperty(obj, key, {
        value: val,
        enumerable: !!enumerable,
        writable: true,
        configurable: true
    });
}

/**
 * Parse simple path.
 */
var bailRE = /[^\w.$]/;
function parsePath (path) {
    if (bailRE.test(path)) {
        return
    }
    var segments = path.split('.');
    return function (obj) {
        for (var i = 0; i < segments.length; i++) {
            if (!obj) { return }
            obj = obj[segments[i]];
        }
        return obj
    }
}

var warn = noop$1;

var formatComponentName;

function handleError (err, vm, info) {
    if (config$1.errorHandler) {
        config$1.errorHandler.call(null, err, vm, info);
    } else {
        if (inBrowser && typeof console !== 'undefined') {
            console.error(err);
        } else {
            throw err
        }
    }
}

/*  */
/* globals MutationObserver */

// can we use __proto__?
var hasProto = '__proto__' in {};

// Browser environment sniffing
var inBrowser = typeof window !== 'undefined';
var UA = inBrowser && window.navigator.userAgent.toLowerCase();
var isIE = UA && /msie|trident/.test(UA);
var isIE9 = UA && UA.indexOf('msie 9.0') > 0;
var isEdge = UA && UA.indexOf('edge/') > 0;
var isAndroid = UA && UA.indexOf('android') > 0;
var isIOS = UA && /iphone|ipad|ipod|ios/.test(UA);
var isChrome = UA && /chrome\/\d+/.test(UA) && !isEdge;

var supportsPassive = false;
if (inBrowser) {
    try {
        var opts = {};
        Object.defineProperty(opts, 'passive', ({
            get: function get () {
                /* istanbul ignore next */
                supportsPassive = true;
            }
        } )); // https://github.com/facebook/flow/issues/285
        window.addEventListener('test-passive', null, opts);
    } catch (e) {}
}

// this needs to be lazy-evaled because vue may be required before
// vue-server-renderer can set VUE_ENV
var _isServer;
var isServerRendering = function () {
    if (_isServer === undefined) {
        /* istanbul ignore if */
        if (!inBrowser && typeof global !== 'undefined') {
            // detect presence of vue-server-renderer and avoid
            // Webpack shimming the process
            _isServer = global['process'].env.VUE_ENV === 'server';
        } else {
            _isServer = false;
        }
    }
    return _isServer
};

// detect devtools
var devtools = inBrowser && window.__VUE_DEVTOOLS_GLOBAL_HOOK__;

/* istanbul ignore next */
function isNative (Ctor) {
    return typeof Ctor === 'function' && /native code/.test(Ctor.toString())
}

var hasSymbol =
    typeof Symbol !== 'undefined' && isNative(Symbol) &&
    typeof Reflect !== 'undefined' && isNative(Reflect.ownKeys);

/**
 * Defer a task to execute it asynchronously.
 */
var nextTick$1 = (function () {
    var callbacks = [];
    var pending = false;
    var timerFunc;

    function nextTickHandler () {
        pending = false;
        var copies = callbacks.slice(0);
        callbacks.length = 0;
        for (var i = 0; i < copies.length; i++) {
            copies[i]();
        }
    }

    // the nextTick behavior leverages the microtask queue, which can be accessed
    // via either native Promise.then or MutationObserver.
    // MutationObserver has wider support, however it is seriously bugged in
    // UIWebView in iOS >= 9.3.3 when triggered in touch event handlers. It
    // completely stops working after triggering a few times... so, if native
    // Promise is available, we will use it:
    /* istanbul ignore if */
    if (typeof Promise !== 'undefined' && isNative(Promise)) {
        var p = Promise.resolve();
        var logError = function (err) { console.error(err); };
        timerFunc = function () {
            p.then(nextTickHandler).catch(logError);
            // in problematic UIWebViews, Promise.then doesn't completely break, but
            // it can get stuck in a weird state where callbacks are pushed into the
            // microtask queue but the queue isn't being flushed, until the browser
            // needs to do some other work, e.g. handle a timer. Therefore we can
            // "force" the microtask queue to be flushed by adding an empty timer.
            if (isIOS) { setTimeout(noop$1); }
        };
    } else if (typeof MutationObserver !== 'undefined' && (
        isNative(MutationObserver) ||
        // PhantomJS and iOS 7.x
        MutationObserver.toString() === '[object MutationObserverConstructor]'
    )) {
        // use MutationObserver where native Promise is not available,
        // e.g. PhantomJS IE11, iOS7, Android 4.4
        var counter = 1;
        var observer = new MutationObserver(nextTickHandler);
        var textNode = document.createTextNode(String(counter));
        observer.observe(textNode, {
            characterData: true
        });
        timerFunc = function () {
            counter = (counter + 1) % 2;
            textNode.data = String(counter);
        };
    } else {
        // fallback to setTimeout
        /* istanbul ignore next */
        timerFunc = function () {
            setTimeout(nextTickHandler, 0);
        };
    }

    return function queueNextTick (cb, ctx) {
        var _resolve;
        callbacks.push(function () {
            if (cb) {
                try {
                    cb.call(ctx);
                } catch (e) {
                    handleError(e, ctx, 'nextTick');
                }
            } else if (_resolve) {
                _resolve(ctx);
            }
        });
        if (!pending) {
            pending = true;
            timerFunc();
        }
        if (!cb && typeof Promise !== 'undefined') {
            return new Promise(function (resolve, reject) {
                _resolve = resolve;
            })
        }
    }
})();

var _Set;
/* istanbul ignore if */
if (typeof Set !== 'undefined' && isNative(Set)) {
    // use native Set when available.
    _Set = Set;
} else {
    // a non-standard Set polyfill that only works with primitive keys.
    _Set = (function () {
        function Set () {
            this.set = Object.create(null);
        }
        Set.prototype.has = function has (key) {
            return this.set[key] === true
        };
        Set.prototype.add = function add (key) {
            this.set[key] = true;
        };
        Set.prototype.clear = function clear () {
            this.set = Object.create(null);
        };

        return Set;
    }());
}

/*  */


var uid = 0;

/**
 * A dep is an observable that can have multiple
 * directives subscribing to it.
 */
var Dep = function Dep () {
    this.id = uid++;
    this.subs = [];
};

Dep.prototype.addSub = function addSub (sub) {
    this.subs.push(sub);
};

Dep.prototype.removeSub = function removeSub (sub) {
    remove(this.subs, sub);
};

Dep.prototype.depend = function depend () {
    if (Dep.target) {
        Dep.target.addDep(this);
    }
};

Dep.prototype.notify = function notify () {
    // stabilize the subscriber list first
    var subs = this.subs.slice();
    for (var i = 0, l = subs.length; i < l; i++) {
        subs[i].update();
    }
};

// the current target watcher being evaluated.
// this is globally unique because there could be only one
// watcher being evaluated at any time.
Dep.target = null;
var targetStack = [];

function pushTarget (_target) {
    if (Dep.target) { targetStack.push(Dep.target); }
    Dep.target = _target;
}

function popTarget () {
    Dep.target = targetStack.pop();
}

/*
 * not type checking this file because flow doesn't play well with
 * dynamically accessing methods on Array prototype
 */

var arrayProto = Array.prototype;
var arrayMethods = Object.create(arrayProto);[
    'push',
    'pop',
    'shift',
    'unshift',
    'splice',
    'sort',
    'reverse'
]
    .forEach(function (method) {
        // cache original method
        var original = arrayProto[method];
        def(arrayMethods, method, function mutator () {
            var arguments$1 = arguments;

            // avoid leaking arguments:
            // http://jsperf.com/closure-with-arguments
            var i = arguments.length;
            var args = new Array(i);
            while (i--) {
                args[i] = arguments$1[i];
            }
            var result = original.apply(this, args);
            var ob = this.__ob__;
            var inserted;
            switch (method) {
                case 'push':
                    inserted = args;
                    break
                case 'unshift':
                    inserted = args;
                    break
                case 'splice':
                    inserted = args.slice(2);
                    break
            }
            if (inserted) { ob.observeArray(inserted); }
            // notify change
            ob.dep.notify();
            return result
        });
    });

/*  */

var arrayKeys = Object.getOwnPropertyNames(arrayMethods);

/**
 * By default, when a reactive property is set, the new value is
 * also converted to become reactive. However when passing down props,
 * we don't want to force conversion because the value may be a nested value
 * under a frozen data structure. Converting it would defeat the optimization.
 */
var observerState = {
    shouldConvert: true,
    isSettingProps: false
};

/**
 * Observer class that are attached to each observed
 * object. Once attached, the observer converts target
 * object's property keys into getter/setters that
 * collect dependencies and dispatches updates.
 */
var Observer = function Observer (value) {
    this.value = value;
    this.dep = new Dep();
    this.vmCount = 0;
    def(value, '__ob__', this);
    if (Array.isArray(value)) {
        var augment = hasProto
            ? protoAugment
            : copyAugment;
        augment(value, arrayMethods, arrayKeys);
        this.observeArray(value);
    } else {
        this.walk(value);
    }
};

/**
 * Walk through each property and convert them into
 * getter/setters. This method should only be called when
 * value type is Object.
 */
Observer.prototype.walk = function walk (obj) {
    var keys = Object.keys(obj);
    for (var i = 0; i < keys.length; i++) {
        defineReactive$$1(obj, keys[i], obj[keys[i]]);
    }
};

/**
 * Observe a list of Array items.
 */
Observer.prototype.observeArray = function observeArray (items) {
    for (var i = 0, l = items.length; i < l; i++) {
        observe(items[i]);
    }
};

// helpers

/**
 * Augment an target Object or Array by intercepting
 * the prototype chain using __proto__
 */
function protoAugment (target, src) {
    /* eslint-disable no-proto */
    target.__proto__ = src;
    /* eslint-enable no-proto */
}

/**
 * Augment an target Object or Array by defining
 * hidden properties.
 */
/* istanbul ignore next */
function copyAugment (target, src, keys) {
    for (var i = 0, l = keys.length; i < l; i++) {
        var key = keys[i];
        def(target, key, src[key]);
    }
}

/**
 * Attempt to create an observer instance for a value,
 * returns the new observer if successfully observed,
 * or the existing observer if the value already has one.
 */
function observe (value, asRootData) {
    if (!isObject(value)) {
        return
    }
    var ob;
    if (hasOwn(value, '__ob__') && value.__ob__ instanceof Observer) {
        ob = value.__ob__;
    } else if (
        observerState.shouldConvert &&
        !isServerRendering() &&
        (Array.isArray(value) || isPlainObject(value)) &&
        Object.isExtensible(value) &&
        !value._isVue
    ) {
        ob = new Observer(value);
    }
    if (asRootData && ob) {
        ob.vmCount++;
    }
    return ob
}

/**
 * Define a reactive property on an Object.
 */
function defineReactive$$1 (
    obj,
    key,
    val,
    customSetter
) {
    var dep = new Dep();

    var property = Object.getOwnPropertyDescriptor(obj, key);
    if (property && property.configurable === false) {
        return
    }

    // cater for pre-defined getter/setters
    var getter = property && property.get;
    var setter = property && property.set;

    var childOb = observe(val);
    Object.defineProperty(obj, key, {
        enumerable: true,
        configurable: true,
        get: function reactiveGetter () {
            var value = getter ? getter.call(obj) : val;
            if (Dep.target) {
                dep.depend();
                if (childOb) {
                    childOb.dep.depend();
                }
                if (Array.isArray(value)) {
                    dependArray(value);
                }
            }
            return value
        },
        set: function reactiveSetter (newVal) {
            var value = getter ? getter.call(obj) : val;
            /* eslint-disable no-self-compare */
            if (newVal === value || (newVal !== newVal && value !== value)) {
                return
            }
            /* eslint-enable no-self-compare */
            if ("production" !== 'production' && customSetter) {
                customSetter();
            }
            if (setter) {
                setter.call(obj, newVal);
            } else {
                val = newVal;
            }
            childOb = observe(newVal);
            dep.notify();
        }
    });
}

/**
 * Set a property on an object. Adds the new property and
 * triggers change notification if the property doesn't
 * already exist.
 */
function set (target, key, val) {
    if (Array.isArray(target) && typeof key === 'number') {
        target.length = Math.max(target.length, key);
        target.splice(key, 1, val);
        return val
    }
    if (hasOwn(target, key)) {
        target[key] = val;
        return val
    }
    var ob = (target ).__ob__;
    if (target._isVue || (ob && ob.vmCount)) {
        "production" !== 'production' && warn(
            'Avoid adding reactive properties to a Vue instance or its root $data ' +
            'at runtime - declare it upfront in the data option.'
        );
        return val
    }
    if (!ob) {
        target[key] = val;
        return val
    }
    defineReactive$$1(ob.value, key, val);
    ob.dep.notify();
    return val
}

/**
 * Delete a property and trigger change if necessary.
 */
function del (target, key) {
    if (Array.isArray(target) && typeof key === 'number') {
        target.splice(key, 1);
        return
    }
    var ob = (target ).__ob__;
    if (target._isVue || (ob && ob.vmCount)) {
        "production" !== 'production' && warn(
            'Avoid deleting properties on a Vue instance or its root $data ' +
            '- just set it to null.'
        );
        return
    }
    if (!hasOwn(target, key)) {
        return
    }
    delete target[key];
    if (!ob) {
        return
    }
    ob.dep.notify();
}

/**
 * Collect dependencies on array elements when the array is touched, since
 * we cannot intercept array element access like property getters.
 */
function dependArray (value) {
    for (var e = (void 0), i = 0, l = value.length; i < l; i++) {
        e = value[i];
        e && e.__ob__ && e.__ob__.dep.depend();
        if (Array.isArray(e)) {
            dependArray(e);
        }
    }
}

/*  */

/**
 * Option overwriting strategies are functions that handle
 * how to merge a parent option value and a child option
 * value into the final value.
 */
var strats = config$1.optionMergeStrategies;

/**
 * Options with restrictions
 */
/**
 * Helper that recursively merges two data objects together.
 */
function mergeData (to, from) {
    if (!from) { return to }
    var key, toVal, fromVal;
    var keys = Object.keys(from);
    for (var i = 0; i < keys.length; i++) {
        key = keys[i];
        toVal = to[key];
        fromVal = from[key];
        if (!hasOwn(to, key)) {
            set(to, key, fromVal);
        } else if (isPlainObject(toVal) && isPlainObject(fromVal)) {
            mergeData(toVal, fromVal);
        }
    }
    return to
}

/**
 * Data
 */
strats.data = function (
    parentVal,
    childVal,
    vm
) {
    if (!vm) {
        // in a Vue.extend merge, both should be functions
        if (!childVal) {
            return parentVal
        }
        if (typeof childVal !== 'function') {
            "production" !== 'production' && warn(
                'The "data" option should be a function ' +
                'that returns a per-instance value in component ' +
                'definitions.',
                vm
            );
            return parentVal
        }
        if (!parentVal) {
            return childVal
        }
        // when parentVal & childVal are both present,
        // we need to return a function that returns the
        // merged result of both functions... no need to
        // check if parentVal is a function here because
        // it has to be a function to pass previous merges.
        return function mergedDataFn () {
            return mergeData(
                childVal.call(this),
                parentVal.call(this)
            )
        }
    } else if (parentVal || childVal) {
        return function mergedInstanceDataFn () {
            // instance merge
            var instanceData = typeof childVal === 'function'
                ? childVal.call(vm)
                : childVal;
            var defaultData = typeof parentVal === 'function'
                ? parentVal.call(vm)
                : undefined;
            if (instanceData) {
                return mergeData(instanceData, defaultData)
            } else {
                return defaultData
            }
        }
    }
};

/**
 * Hooks and props are merged as arrays.
 */
function mergeHook (
    parentVal,
    childVal
) {
    return childVal
        ? parentVal
            ? parentVal.concat(childVal)
            : Array.isArray(childVal)
                ? childVal
                : [childVal]
        : parentVal
}

LIFECYCLE_HOOKS.forEach(function (hook) {
    strats[hook] = mergeHook;
});

/**
 * Assets
 *
 * When a vm is present (instance creation), we need to do
 * a three-way merge between constructor options, instance
 * options and parent options.
 */
function mergeAssets (parentVal, childVal) {
    var res = Object.create(parentVal || null);
    return childVal
        ? extend(res, childVal)
        : res
}

ASSET_TYPES.forEach(function (type) {
    strats[type + 's'] = mergeAssets;
});

/**
 * Watchers.
 *
 * Watchers hashes should not overwrite one
 * another, so we merge them as arrays.
 */
strats.watch = function (parentVal, childVal) {
    /* istanbul ignore if */
    if (!childVal) { return Object.create(parentVal || null) }
    if (!parentVal) { return childVal }
    var ret = {};
    extend(ret, parentVal);
    for (var key in childVal) {
        var parent = ret[key];
        var child = childVal[key];
        if (parent && !Array.isArray(parent)) {
            parent = [parent];
        }
        ret[key] = parent
            ? parent.concat(child)
            : [child];
    }
    return ret
};

/**
 * Other object hashes.
 */
strats.props =
    strats.methods =
        strats.computed = function (parentVal, childVal) {
            if (!childVal) { return Object.create(parentVal || null) }
            if (!parentVal) { return childVal }
            var ret = Object.create(null);
            extend(ret, parentVal);
            extend(ret, childVal);
            return ret
        };

/**
 * Default strategy.
 */
var defaultStrat = function (parentVal, childVal) {
    return childVal === undefined
        ? parentVal
        : childVal
};

/**
 * Ensure all props option syntax are normalized into the
 * Object-based format.
 */
function normalizeProps (options) {
    var props = options.props;
    if (!props) { return }
    var res = {};
    var i, val, name;
    if (Array.isArray(props)) {
        i = props.length;
        while (i--) {
            val = props[i];
            if (typeof val === 'string') {
                name = camelize(val);
                res[name] = { type: null };
            } else {}
        }
    } else if (isPlainObject(props)) {
        for (var key in props) {
            val = props[key];
            name = camelize(key);
            res[name] = isPlainObject(val)
                ? val
                : { type: val };
        }
    }
    options.props = res;
}

/**
 * Normalize raw function directives into object format.
 */
function normalizeDirectives (options) {
    var dirs = options.directives;
    if (dirs) {
        for (var key in dirs) {
            var def = dirs[key];
            if (typeof def === 'function') {
                dirs[key] = { bind: def, update: def };
            }
        }
    }
}

/**
 * Merge two option objects into a new one.
 * Core utility used in both instantiation and inheritance.
 */
function mergeOptions (
    parent,
    child,
    vm
) {
    if (typeof child === 'function') {
        child = child.options;
    }

    normalizeProps(child);
    normalizeDirectives(child);
    var extendsFrom = child.extends;
    if (extendsFrom) {
        parent = mergeOptions(parent, extendsFrom, vm);
    }
    if (child.mixins) {
        for (var i = 0, l = child.mixins.length; i < l; i++) {
            parent = mergeOptions(parent, child.mixins[i], vm);
        }
    }
    var options = {};
    var key;
    for (key in parent) {
        mergeField(key);
    }
    for (key in child) {
        if (!hasOwn(parent, key)) {
            mergeField(key);
        }
    }
    function mergeField (key) {
        var strat = strats[key] || defaultStrat;
        options[key] = strat(parent[key], child[key], vm, key);
    }
    return options
}

/**
 * Resolve an asset.
 * This function is used because child instances need access
 * to assets defined in its ancestor chain.
 */
function resolveAsset (
    options,
    type,
    id,
    warnMissing
) {
    /* istanbul ignore if */
    if (typeof id !== 'string') {
        return
    }
    var assets = options[type];
    // check local registration variations first
    if (hasOwn(assets, id)) { return assets[id] }
    var camelizedId = camelize(id);
    if (hasOwn(assets, camelizedId)) { return assets[camelizedId] }
    var PascalCaseId = capitalize(camelizedId);
    if (hasOwn(assets, PascalCaseId)) { return assets[PascalCaseId] }
    // fallback to prototype chain
    var res = assets[id] || assets[camelizedId] || assets[PascalCaseId];
    if ("production" !== 'production' && warnMissing && !res) {
        warn(
            'Failed to resolve ' + type.slice(0, -1) + ': ' + id,
            options
        );
    }
    return res
}

/*  */

function validateProp (
    key,
    propOptions,
    propsData,
    vm
) {
    var prop = propOptions[key];
    var absent = !hasOwn(propsData, key);
    var value = propsData[key];
    // handle boolean props
    if (isType(Boolean, prop.type)) {
        if (absent && !hasOwn(prop, 'default')) {
            value = false;
        } else if (!isType(String, prop.type) && (value === '' || value === hyphenate(key))) {
            value = true;
        }
    }
    // check default value
    if (value === undefined) {
        value = getPropDefaultValue(vm, prop, key);
        // since the default value is a fresh copy,
        // make sure to observe it.
        var prevShouldConvert = observerState.shouldConvert;
        observerState.shouldConvert = true;
        observe(value);
        observerState.shouldConvert = prevShouldConvert;
    }
    return value
}

/**
 * Get the default value of a prop.
 */
function getPropDefaultValue (vm, prop, key) {
    // no default, return undefined
    if (!hasOwn(prop, 'default')) {
        return undefined
    }
    var def = prop.default;
    // warn against non-factory defaults for Object & Array
    if ("production" !== 'production' && isObject(def)) {
        warn(
            'Invalid default value for prop "' + key + '": ' +
            'Props with type Object/Array must use a factory function ' +
            'to return the default value.',
            vm
        );
    }
    // the raw prop value was also undefined from previous render,
    // return previous default value to avoid unnecessary watcher trigger
    if (vm && vm.$options.propsData &&
        vm.$options.propsData[key] === undefined &&
        vm._props[key] !== undefined) {
        return vm._props[key]
    }
    // call factory function for non-Function types
    // a value is Function if its prototype is function even across different execution context
    return typeof def === 'function' && getType(prop.type) !== 'Function'
        ? def.call(vm)
        : def
}

/**
 * Use function string name to check built-in types,
 * because a simple equality check will fail when running
 * across different vms / iframes.
 */
function getType (fn) {
    var match = fn && fn.toString().match(/^\s*function (\w+)/);
    return match ? match[1] : ''
}

function isType (type, fn) {
    if (!Array.isArray(fn)) {
        return getType(fn) === getType(type)
    }
    for (var i = 0, len = fn.length; i < len; i++) {
        if (getType(fn[i]) === getType(type)) {
            return true
        }
    }
    /* istanbul ignore next */
    return false
}

var mark;
var measure;

/* not type checking this file because flow doesn't play well with Proxy */

/*  */

var VNode = function VNode (
    tag,
    data,
    children,
    text,
    elm,
    context,
    componentOptions
) {
    this.tag = tag;
    this.data = data;
    this.children = children;
    this.text = text;
    this.elm = elm;
    this.ns = undefined;
    this.context = context;
    this.functionalContext = undefined;
    this.key = data && data.key;
    this.componentOptions = componentOptions;
    this.componentInstance = undefined;
    this.parent = undefined;
    this.raw = false;
    this.isStatic = false;
    this.isRootInsert = true;
    this.isComment = false;
    this.isCloned = false;
    this.isOnce = false;
};

var prototypeAccessors = { child: {} };

// DEPRECATED: alias for componentInstance for backwards compat.
/* istanbul ignore next */
prototypeAccessors.child.get = function () {
    return this.componentInstance
};

Object.defineProperties( VNode.prototype, prototypeAccessors );

var createEmptyVNode = function () {
    var node = new VNode();
    node.text = '';
    node.isComment = true;
    return node
};

function createTextVNode (val) {
    return new VNode(undefined, undefined, undefined, String(val))
}

// optimized shallow clone
// used for static nodes and slot nodes because they may be reused across
// multiple renders, cloning them avoids errors when DOM manipulations rely
// on their elm reference.
function cloneVNode (vnode) {
    var cloned = new VNode(
        vnode.tag,
        vnode.data,
        vnode.children,
        vnode.text,
        vnode.elm,
        vnode.context,
        vnode.componentOptions
    );
    cloned.ns = vnode.ns;
    cloned.isStatic = vnode.isStatic;
    cloned.key = vnode.key;
    cloned.isCloned = true;
    return cloned
}

function cloneVNodes (vnodes) {
    var len = vnodes.length;
    var res = new Array(len);
    for (var i = 0; i < len; i++) {
        res[i] = cloneVNode(vnodes[i]);
    }
    return res
}

/*  */

var normalizeEvent = cached(function (name) {
    var passive = name.charAt(0) === '&';
    name = passive ? name.slice(1) : name;
    var once$$1 = name.charAt(0) === '~'; // Prefixed last, checked first
    name = once$$1 ? name.slice(1) : name;
    var capture = name.charAt(0) === '!';
    name = capture ? name.slice(1) : name;
    return {
        name: name,
        once: once$$1,
        capture: capture,
        passive: passive
    }
});

function createFnInvoker (fns) {
    function invoker () {
        var arguments$1 = arguments;

        var fns = invoker.fns;
        if (Array.isArray(fns)) {
            for (var i = 0; i < fns.length; i++) {
                fns[i].apply(null, arguments$1);
            }
        } else {
            // return handler return value for single handlers
            return fns.apply(null, arguments)
        }
    }
    invoker.fns = fns;
    return invoker
}

function updateListeners (
    on,
    oldOn,
    add,
    remove$$1,
    vm
) {
    var name, cur, old, event;
    for (name in on) {
        cur = on[name];
        old = oldOn[name];
        event = normalizeEvent(name);
        if (isUndef(cur)) {
            "production" !== 'production' && warn(
                "Invalid handler for event \"" + (event.name) + "\": got " + String(cur),
                vm
            );
        } else if (isUndef(old)) {
            if (isUndef(cur.fns)) {
                cur = on[name] = createFnInvoker(cur);
            }
            add(event.name, cur, event.once, event.capture, event.passive);
        } else if (cur !== old) {
            old.fns = cur;
            on[name] = old;
        }
    }
    for (name in oldOn) {
        if (isUndef(on[name])) {
            event = normalizeEvent(name);
            remove$$1(event.name, oldOn[name], event.capture);
        }
    }
}

/*  */

function mergeVNodeHook (def, hookKey, hook) {
    var invoker;
    var oldHook = def[hookKey];

    function wrappedHook () {
        hook.apply(this, arguments);
        // important: remove merged hook to ensure it's called only once
        // and prevent memory leak
        remove(invoker.fns, wrappedHook);
    }

    if (isUndef(oldHook)) {
        // no existing hook
        invoker = createFnInvoker([wrappedHook]);
    } else {
        /* istanbul ignore if */
        if (isDef(oldHook.fns) && isTrue(oldHook.merged)) {
            // already a merged invoker
            invoker = oldHook;
            invoker.fns.push(wrappedHook);
        } else {
            // existing plain hook
            invoker = createFnInvoker([oldHook, wrappedHook]);
        }
    }

    invoker.merged = true;
    def[hookKey] = invoker;
}

/*  */

function extractPropsFromVNodeData (
    data,
    Ctor,
    tag
) {
    // we are only extracting raw values here.
    // validation and default values are handled in the child
    // component itself.
    var propOptions = Ctor.options.props;
    if (isUndef(propOptions)) {
        return
    }
    var res = {};
    var attrs = data.attrs;
    var props = data.props;
    if (isDef(attrs) || isDef(props)) {
        for (var key in propOptions) {
            var altKey = hyphenate(key);
            checkProp(res, props, key, altKey, true) ||
            checkProp(res, attrs, key, altKey, false);
        }
    }
    return res
}

function checkProp (
    res,
    hash,
    key,
    altKey,
    preserve
) {
    if (isDef(hash)) {
        if (hasOwn(hash, key)) {
            res[key] = hash[key];
            if (!preserve) {
                delete hash[key];
            }
            return true
        } else if (hasOwn(hash, altKey)) {
            res[key] = hash[altKey];
            if (!preserve) {
                delete hash[altKey];
            }
            return true
        }
    }
    return false
}

/*  */

// The template compiler attempts to minimize the need for normalization by
// statically analyzing the template at compile time.
//
// For plain HTML markup, normalization can be completely skipped because the
// generated render function is guaranteed to return Array<VNode>. There are
// two cases where extra normalization is needed:

// 1. When the children contains components - because a functional component
// may return an Array instead of a single root. In this case, just a simple
// normalization is needed - if any child is an Array, we flatten the whole
// thing with Array.prototype.concat. It is guaranteed to be only 1-level deep
// because functional components already normalize their own children.
function simpleNormalizeChildren (children) {
    for (var i = 0; i < children.length; i++) {
        if (Array.isArray(children[i])) {
            return Array.prototype.concat.apply([], children)
        }
    }
    return children
}

// 2. When the children contains constructs that always generated nested Arrays,
// e.g. <template>, <slot>, v-for, or when the children is provided by user
// with hand-written render functions / JSX. In such cases a full normalization
// is needed to cater to all possible types of children values.
function normalizeChildren (children) {
    return isPrimitive(children)
        ? [createTextVNode(children)]
        : Array.isArray(children)
            ? normalizeArrayChildren(children)
            : undefined
}

function normalizeArrayChildren (children, nestedIndex) {
    var res = [];
    var i, c, last;
    for (i = 0; i < children.length; i++) {
        c = children[i];
        if (isUndef(c) || typeof c === 'boolean') { continue }
        last = res[res.length - 1];
        //  nested
        if (Array.isArray(c)) {
            res.push.apply(res, normalizeArrayChildren(c, ((nestedIndex || '') + "_" + i)));
        } else if (isPrimitive(c)) {
            if (isDef(last) && isDef(last.text)) {
                (last).text += String(c);
            } else if (c !== '') {
                // convert primitive to vnode
                res.push(createTextVNode(c));
            }
        } else {
            if (isDef(c.text) && isDef(last) && isDef(last.text)) {
                res[res.length - 1] = createTextVNode(last.text + c.text);
            } else {
                // default key for nested array children (likely generated by v-for)
                if (isDef(c.tag) && isUndef(c.key) && isDef(nestedIndex)) {
                    c.key = "__vlist" + ((nestedIndex)) + "_" + i + "__";
                }
                res.push(c);
            }
        }
    }
    return res
}

/*  */

function ensureCtor (comp, base) {
    return isObject(comp)
        ? base.extend(comp)
        : comp
}

function resolveAsyncComponent (
    factory,
    baseCtor,
    context
) {
    if (isTrue(factory.error) && isDef(factory.errorComp)) {
        return factory.errorComp
    }

    if (isDef(factory.resolved)) {
        return factory.resolved
    }

    if (isTrue(factory.loading) && isDef(factory.loadingComp)) {
        return factory.loadingComp
    }

    if (isDef(factory.contexts)) {
        // already pending
        factory.contexts.push(context);
    } else {
        var contexts = factory.contexts = [context];
        var sync = true;

        var forceRender = function () {
            for (var i = 0, l = contexts.length; i < l; i++) {
                contexts[i].$forceUpdate();
            }
        };

        var resolve = once$1(function (res) {
            // cache resolved
            factory.resolved = ensureCtor(res, baseCtor);
            // invoke callbacks only if this is not a synchronous resolve
            // (async resolves are shimmed as synchronous during SSR)
            if (!sync) {
                forceRender();
            }
        });

        var reject = once$1(function (reason) {
            "production" !== 'production' && warn(
                "Failed to resolve async component: " + (String(factory)) +
                (reason ? ("\nReason: " + reason) : '')
            );
            if (isDef(factory.errorComp)) {
                factory.error = true;
                forceRender();
            }
        });

        var res = factory(resolve, reject);

        if (isObject(res)) {
            if (typeof res.then === 'function') {
                // () => Promise
                if (isUndef(factory.resolved)) {
                    res.then(resolve, reject);
                }
            } else if (isDef(res.component) && typeof res.component.then === 'function') {
                res.component.then(resolve, reject);

                if (isDef(res.error)) {
                    factory.errorComp = ensureCtor(res.error, baseCtor);
                }

                if (isDef(res.loading)) {
                    factory.loadingComp = ensureCtor(res.loading, baseCtor);
                    if (res.delay === 0) {
                        factory.loading = true;
                    } else {
                        setTimeout(function () {
                            if (isUndef(factory.resolved) && isUndef(factory.error)) {
                                factory.loading = true;
                                forceRender();
                            }
                        }, res.delay || 200);
                    }
                }

                if (isDef(res.timeout)) {
                    setTimeout(function () {
                        reject(
                            null
                        );
                    }, res.timeout);
                }
            }
        }

        sync = false;
        // return in case resolved synchronously
        return factory.loading
            ? factory.loadingComp
            : factory.resolved
    }
}

/*  */

function getFirstComponentChild (children) {
    if (Array.isArray(children)) {
        for (var i = 0; i < children.length; i++) {
            var c = children[i];
            if (isDef(c) && isDef(c.componentOptions)) {
                return c
            }
        }
    }
}

/*  */

/*  */

function initEvents (vm) {
    vm._events = Object.create(null);
    vm._hasHookEvent = false;
    // init parent attached events
    var listeners = vm.$options._parentListeners;
    if (listeners) {
        updateComponentListeners(vm, listeners);
    }
}

var target;

function add (event, fn, once$$1) {
    if (once$$1) {
        target.$once(event, fn);
    } else {
        target.$on(event, fn);
    }
}

function remove$1 (event, fn) {
    target.$off(event, fn);
}

function updateComponentListeners (
    vm,
    listeners,
    oldListeners
) {
    target = vm;
    updateListeners(listeners, oldListeners || {}, add, remove$1, vm);
}

function eventsMixin (Vue) {
    var hookRE = /^hook:/;
    Vue.prototype.$on = function (event, fn) {
        var this$1 = this;

        var vm = this;
        if (Array.isArray(event)) {
            for (var i = 0, l = event.length; i < l; i++) {
                this$1.$on(event[i], fn);
            }
        } else {
            (vm._events[event] || (vm._events[event] = [])).push(fn);
            // optimize hook:event cost by using a boolean flag marked at registration
            // instead of a hash lookup
            if (hookRE.test(event)) {
                vm._hasHookEvent = true;
            }
        }
        return vm
    };

    Vue.prototype.$once = function (event, fn) {
        var vm = this;
        function on () {
            vm.$off(event, on);
            fn.apply(vm, arguments);
        }
        on.fn = fn;
        vm.$on(event, on);
        return vm
    };

    Vue.prototype.$off = function (event, fn) {
        var this$1 = this;

        var vm = this;
        // all
        if (!arguments.length) {
            vm._events = Object.create(null);
            return vm
        }
        // array of events
        if (Array.isArray(event)) {
            for (var i$1 = 0, l = event.length; i$1 < l; i$1++) {
                this$1.$off(event[i$1], fn);
            }
            return vm
        }
        // specific event
        var cbs = vm._events[event];
        if (!cbs) {
            return vm
        }
        if (arguments.length === 1) {
            vm._events[event] = null;
            return vm
        }
        // specific handler
        var cb;
        var i = cbs.length;
        while (i--) {
            cb = cbs[i];
            if (cb === fn || cb.fn === fn) {
                cbs.splice(i, 1);
                break
            }
        }
        return vm
    };

    Vue.prototype.$emit = function (event) {
        var vm = this;
        var cbs = vm._events[event];
        if (cbs) {
            cbs = cbs.length > 1 ? toArray(cbs) : cbs;
            var args = toArray(arguments, 1);
            for (var i = 0, l = cbs.length; i < l; i++) {
                cbs[i].apply(vm, args);
            }
        }
        return vm
    };
}

/*  */

/**
 * Runtime helper for resolving raw children VNodes into a slot object.
 */
function resolveSlots (
    children,
    context
) {
    var slots = {};
    if (!children) {
        return slots
    }
    var defaultSlot = [];
    for (var i = 0, l = children.length; i < l; i++) {
        var child = children[i];
        // named slots should only be respected if the vnode was rendered in the
        // same context.
        if ((child.context === context || child.functionalContext === context) &&
            child.data && child.data.slot != null) {
            var name = child.data.slot;
            var slot = (slots[name] || (slots[name] = []));
            if (child.tag === 'template') {
                slot.push.apply(slot, child.children);
            } else {
                slot.push(child);
            }
        } else {
            defaultSlot.push(child);
        }
    }
    // ignore whitespace
    if (!defaultSlot.every(isWhitespace)) {
        slots.default = defaultSlot;
    }
    return slots
}

function isWhitespace (node) {
    return node.isComment || node.text === ' '
}

function resolveScopedSlots (
    fns
) {
    var res = {};
    for (var i = 0; i < fns.length; i++) {
        res[fns[i][0]] = fns[i][1];
    }
    return res
}

/*  */

var activeInstance = null;

function initLifecycle (vm) {
    var options = vm.$options;

    // locate first non-abstract parent
    var parent = options.parent;
    if (parent && !options.abstract) {
        while (parent.$options.abstract && parent.$parent) {
            parent = parent.$parent;
        }
        parent.$children.push(vm);
    }

    vm.$parent = parent;
    vm.$root = parent ? parent.$root : vm;

    vm.$children = [];
    vm.$refs = {};

    vm._watcher = null;
    vm._inactive = null;
    vm._directInactive = false;
    vm._isMounted = false;
    vm._isDestroyed = false;
    vm._isBeingDestroyed = false;
}

function lifecycleMixin (Vue) {
    Vue.prototype._update = function (vnode, hydrating) {
        var vm = this;
        if (vm._isMounted) {
            callHook(vm, 'beforeUpdate');
        }
        var prevEl = vm.$el;
        var prevVnode = vm._vnode;
        var prevActiveInstance = activeInstance;
        activeInstance = vm;
        vm._vnode = vnode;
        // Vue.prototype.__patch__ is injected in entry points
        // based on the rendering backend used.
        if (!prevVnode) {
            // initial render
            vm.$el = vm.__patch__(
                vm.$el, vnode, hydrating, false /* removeOnly */,
                vm.$options._parentElm,
                vm.$options._refElm
            );
        } else {
            // updates
            vm.$el = vm.__patch__(prevVnode, vnode);
        }
        activeInstance = prevActiveInstance;
        // update __vue__ reference
        if (prevEl) {
            prevEl.__vue__ = null;
        }
        if (vm.$el) {
            vm.$el.__vue__ = vm;
        }
        // if parent is an HOC, update its $el as well
        if (vm.$vnode && vm.$parent && vm.$vnode === vm.$parent._vnode) {
            vm.$parent.$el = vm.$el;
        }
        // updated hook is called by the scheduler to ensure that children are
        // updated in a parent's updated hook.
    };

    Vue.prototype.$forceUpdate = function () {
        var vm = this;
        if (vm._watcher) {
            vm._watcher.update();
        }
    };

    Vue.prototype.$destroy = function () {
        var vm = this;
        if (vm._isBeingDestroyed) {
            return
        }
        callHook(vm, 'beforeDestroy');
        vm._isBeingDestroyed = true;
        // remove self from parent
        var parent = vm.$parent;
        if (parent && !parent._isBeingDestroyed && !vm.$options.abstract) {
            remove(parent.$children, vm);
        }
        // teardown watchers
        if (vm._watcher) {
            vm._watcher.teardown();
        }
        var i = vm._watchers.length;
        while (i--) {
            vm._watchers[i].teardown();
        }
        // remove reference from data ob
        // frozen object may not have observer.
        if (vm._data.__ob__) {
            vm._data.__ob__.vmCount--;
        }
        // call the last hook...
        vm._isDestroyed = true;
        // invoke destroy hooks on current rendered tree
        vm.__patch__(vm._vnode, null);
        // fire destroyed hook
        callHook(vm, 'destroyed');
        // turn off all instance listeners.
        vm.$off();
        // remove __vue__ reference
        if (vm.$el) {
            vm.$el.__vue__ = null;
        }
        // remove reference to DOM nodes (prevents leak)
        vm.$options._parentElm = vm.$options._refElm = null;
    };
}

function mountComponent (
    vm,
    el,
    hydrating
) {
    vm.$el = el;
    if (!vm.$options.render) {
        vm.$options.render = createEmptyVNode;

    }
    callHook(vm, 'beforeMount');

    var updateComponent;
    /* istanbul ignore if */
    if ("production" !== 'production' && config$1.performance && mark) {
        updateComponent = function () {
            var name = vm._name;
            var id = vm._uid;
            var startTag = "vue-perf-start:" + id;
            var endTag = "vue-perf-end:" + id;

            mark(startTag);
            var vnode = vm._render();
            mark(endTag);
            measure((name + " render"), startTag, endTag);

            mark(startTag);
            vm._update(vnode, hydrating);
            mark(endTag);
            measure((name + " patch"), startTag, endTag);
        };
    } else {
        updateComponent = function () {
            vm._update(vm._render(), hydrating);
        };
    }

    vm._watcher = new Watcher(vm, updateComponent, noop$1);
    hydrating = false;

    // manually mounted instance, call mounted on self
    // mounted is called for render-created child components in its inserted hook
    if (vm.$vnode == null) {
        vm._isMounted = true;
        callHook(vm, 'mounted');
    }
    return vm
}

function updateChildComponent (
    vm,
    propsData,
    listeners,
    parentVnode,
    renderChildren
) {
    // determine whether component has slot children
    // we need to do this before overwriting $options._renderChildren
    var hasChildren = !!(
        renderChildren ||               // has new static slots
        vm.$options._renderChildren ||  // has old static slots
        parentVnode.data.scopedSlots || // has new scoped slots
        vm.$scopedSlots !== emptyObject // has old scoped slots
    );

    vm.$options._parentVnode = parentVnode;
    vm.$vnode = parentVnode; // update vm's placeholder node without re-render
    if (vm._vnode) { // update child tree's parent
        vm._vnode.parent = parentVnode;
    }
    vm.$options._renderChildren = renderChildren;

    // update props
    if (propsData && vm.$options.props) {
        observerState.shouldConvert = false;
        var props = vm._props;
        var propKeys = vm.$options._propKeys || [];
        for (var i = 0; i < propKeys.length; i++) {
            var key = propKeys[i];
            props[key] = validateProp(key, vm.$options.props, propsData, vm);
        }
        observerState.shouldConvert = true;
        vm.$options.propsData = propsData;
    }
    // update listeners
    if (listeners) {
        var oldListeners = vm.$options._parentListeners;
        vm.$options._parentListeners = listeners;
        updateComponentListeners(vm, listeners, oldListeners);
    }
    // resolve slots + force update if has children
    if (hasChildren) {
        vm.$slots = resolveSlots(renderChildren, parentVnode.context);
        vm.$forceUpdate();
    }
}

function isInInactiveTree (vm) {
    while (vm && (vm = vm.$parent)) {
        if (vm._inactive) { return true }
    }
    return false
}

function activateChildComponent (vm, direct) {
    if (direct) {
        vm._directInactive = false;
        if (isInInactiveTree(vm)) {
            return
        }
    } else if (vm._directInactive) {
        return
    }
    if (vm._inactive || vm._inactive === null) {
        vm._inactive = false;
        for (var i = 0; i < vm.$children.length; i++) {
            activateChildComponent(vm.$children[i]);
        }
        callHook(vm, 'activated');
    }
}

function deactivateChildComponent (vm, direct) {
    if (direct) {
        vm._directInactive = true;
        if (isInInactiveTree(vm)) {
            return
        }
    }
    if (!vm._inactive) {
        vm._inactive = true;
        for (var i = 0; i < vm.$children.length; i++) {
            deactivateChildComponent(vm.$children[i]);
        }
        callHook(vm, 'deactivated');
    }
}

function callHook (vm, hook) {
    var handlers = vm.$options[hook];
    if (handlers) {
        for (var i = 0, j = handlers.length; i < j; i++) {
            try {
                handlers[i].call(vm);
            } catch (e) {
                handleError(e, vm, (hook + " hook"));
            }
        }
    }
    if (vm._hasHookEvent) {
        vm.$emit('hook:' + hook);
    }
}

/*  */


var MAX_UPDATE_COUNT = 100;

var queue$1 = [];
var activatedChildren = [];
var has = {};
var circular = {};
var waiting = false;
var flushing = false;
var index = 0;

/**
 * Reset the scheduler's state.
 */
function resetSchedulerState () {
    queue$1.length = activatedChildren.length = 0;
    has = {};
    waiting = flushing = false;
}

/**
 * Flush both queues and run the watchers.
 */
function flushSchedulerQueue () {
    flushing = true;
    var watcher, id;

    // Sort queue before flush.
    // This ensures that:
    // 1. Components are updated from parent to child. (because parent is always
    //    created before the child)
    // 2. A component's user watchers are run before its render watcher (because
    //    user watchers are created before the render watcher)
    // 3. If a component is destroyed during a parent component's watcher run,
    //    its watchers can be skipped.
    queue$1.sort(function (a, b) { return a.id - b.id; });

    // do not cache length because more watchers might be pushed
    // as we run existing watchers
    for (index = 0; index < queue$1.length; index++) {
        watcher = queue$1[index];
        id = watcher.id;
        has[id] = null;
        watcher.run();
        // in dev build, check and stop circular updates.
        if ("production" !== 'production' && has[id] != null) {
            circular[id] = (circular[id] || 0) + 1;
            if (circular[id] > MAX_UPDATE_COUNT) {
                warn(
                    'You may have an infinite update loop ' + (
                        watcher.user
                            ? ("in watcher with expression \"" + (watcher.expression) + "\"")
                            : "in a component render function."
                    ),
                    watcher.vm
                );
                break
            }
        }
    }

    // keep copies of post queues before resetting state
    var activatedQueue = activatedChildren.slice();
    var updatedQueue = queue$1.slice();

    resetSchedulerState();

    // call component updated and activated hooks
    callActivatedHooks(activatedQueue);
    callUpdateHooks(updatedQueue);

    // devtool hook
    /* istanbul ignore if */
    if (devtools && config$1.devtools) {
        devtools.emit('flush');
    }
}

function callUpdateHooks (queue) {
    var i = queue.length;
    while (i--) {
        var watcher = queue[i];
        var vm = watcher.vm;
        if (vm._watcher === watcher && vm._isMounted) {
            callHook(vm, 'updated');
        }
    }
}

/**
 * Queue a kept-alive component that was activated during patch.
 * The queue will be processed after the entire tree has been patched.
 */
function queueActivatedComponent (vm) {
    // setting _inactive to false here so that a render function can
    // rely on checking whether it's in an inactive tree (e.g. router-view)
    vm._inactive = false;
    activatedChildren.push(vm);
}

function callActivatedHooks (queue) {
    for (var i = 0; i < queue.length; i++) {
        queue[i]._inactive = true;
        activateChildComponent(queue[i], true /* true */);
    }
}

/**
 * Push a watcher into the watcher queue.
 * Jobs with duplicate IDs will be skipped unless it's
 * pushed when the queue is being flushed.
 */
function queueWatcher (watcher) {
    var id = watcher.id;
    if (has[id] == null) {
        has[id] = true;
        if (!flushing) {
            queue$1.push(watcher);
        } else {
            // if already flushing, splice the watcher based on its id
            // if already past its id, it will be run next immediately.
            var i = queue$1.length - 1;
            while (i >= 0 && queue$1[i].id > watcher.id) {
                i--;
            }
            queue$1.splice(Math.max(i, index) + 1, 0, watcher);
        }
        // queue the flush
        if (!waiting) {
            waiting = true;
            nextTick$1(flushSchedulerQueue);
        }
    }
}

/*  */

var uid$2 = 0;

/**
 * A watcher parses an expression, collects dependencies,
 * and fires callback when the expression value changes.
 * This is used for both the $watch() api and directives.
 */
var Watcher = function Watcher (
    vm,
    expOrFn,
    cb,
    options
) {
    this.vm = vm;
    vm._watchers.push(this);
    // options
    if (options) {
        this.deep = !!options.deep;
        this.user = !!options.user;
        this.lazy = !!options.lazy;
        this.sync = !!options.sync;
    } else {
        this.deep = this.user = this.lazy = this.sync = false;
    }
    this.cb = cb;
    this.id = ++uid$2; // uid for batching
    this.active = true;
    this.dirty = this.lazy; // for lazy watchers
    this.deps = [];
    this.newDeps = [];
    this.depIds = new _Set();
    this.newDepIds = new _Set();
    this.expression = '';
    // parse expression for getter
    if (typeof expOrFn === 'function') {
        this.getter = expOrFn;
    } else {
        this.getter = parsePath(expOrFn);
        if (!this.getter) {
            this.getter = function () {};
            "production" !== 'production' && warn(
                "Failed watching path: \"" + expOrFn + "\" " +
                'Watcher only accepts simple dot-delimited paths. ' +
                'For full control, use a function instead.',
                vm
            );
        }
    }
    this.value = this.lazy
        ? undefined
        : this.get();
};

/**
 * Evaluate the getter, and re-collect dependencies.
 */
Watcher.prototype.get = function get () {
    pushTarget(this);
    var value;
    var vm = this.vm;
    if (this.user) {
        try {
            value = this.getter.call(vm, vm);
        } catch (e) {
            handleError(e, vm, ("getter for watcher \"" + (this.expression) + "\""));
        }
    } else {
        value = this.getter.call(vm, vm);
    }
    // "touch" every property so they are all tracked as
    // dependencies for deep watching
    if (this.deep) {
        traverse(value);
    }
    popTarget();
    this.cleanupDeps();
    return value
};

/**
 * Add a dependency to this directive.
 */
Watcher.prototype.addDep = function addDep (dep) {
    var id = dep.id;
    if (!this.newDepIds.has(id)) {
        this.newDepIds.add(id);
        this.newDeps.push(dep);
        if (!this.depIds.has(id)) {
            dep.addSub(this);
        }
    }
};

/**
 * Clean up for dependency collection.
 */
Watcher.prototype.cleanupDeps = function cleanupDeps () {
    var this$1 = this;

    var i = this.deps.length;
    while (i--) {
        var dep = this$1.deps[i];
        if (!this$1.newDepIds.has(dep.id)) {
            dep.removeSub(this$1);
        }
    }
    var tmp = this.depIds;
    this.depIds = this.newDepIds;
    this.newDepIds = tmp;
    this.newDepIds.clear();
    tmp = this.deps;
    this.deps = this.newDeps;
    this.newDeps = tmp;
    this.newDeps.length = 0;
};

/**
 * Subscriber interface.
 * Will be called when a dependency changes.
 */
Watcher.prototype.update = function update () {
    /* istanbul ignore else */
    if (this.lazy) {
        this.dirty = true;
    } else if (this.sync) {
        this.run();
    } else {
        queueWatcher(this);
    }
};

/**
 * Scheduler job interface.
 * Will be called by the scheduler.
 */
Watcher.prototype.run = function run () {
    if (this.active) {
        var value = this.get();
        if (
            value !== this.value ||
            // Deep watchers and watchers on Object/Arrays should fire even
            // when the value is the same, because the value may
            // have mutated.
            isObject(value) ||
            this.deep
        ) {
            // set new value
            var oldValue = this.value;
            this.value = value;
            if (this.user) {
                try {
                    this.cb.call(this.vm, value, oldValue);
                } catch (e) {
                    handleError(e, this.vm, ("callback for watcher \"" + (this.expression) + "\""));
                }
            } else {
                this.cb.call(this.vm, value, oldValue);
            }
        }
    }
};

/**
 * Evaluate the value of the watcher.
 * This only gets called for lazy watchers.
 */
Watcher.prototype.evaluate = function evaluate () {
    this.value = this.get();
    this.dirty = false;
};

/**
 * Depend on all deps collected by this watcher.
 */
Watcher.prototype.depend = function depend () {
    var this$1 = this;

    var i = this.deps.length;
    while (i--) {
        this$1.deps[i].depend();
    }
};

/**
 * Remove self from all dependencies' subscriber list.
 */
Watcher.prototype.teardown = function teardown () {
    var this$1 = this;

    if (this.active) {
        // remove self from vm's watcher list
        // this is a somewhat expensive operation so we skip it
        // if the vm is being destroyed.
        if (!this.vm._isBeingDestroyed) {
            remove(this.vm._watchers, this);
        }
        var i = this.deps.length;
        while (i--) {
            this$1.deps[i].removeSub(this$1);
        }
        this.active = false;
    }
};

/**
 * Recursively traverse an object to evoke all converted
 * getters, so that every nested property inside the object
 * is collected as a "deep" dependency.
 */
var seenObjects = new _Set();
function traverse (val) {
    seenObjects.clear();
    _traverse(val, seenObjects);
}

function _traverse (val, seen) {
    var i, keys;
    var isA = Array.isArray(val);
    if ((!isA && !isObject(val)) || !Object.isExtensible(val)) {
        return
    }
    if (val.__ob__) {
        var depId = val.__ob__.dep.id;
        if (seen.has(depId)) {
            return
        }
        seen.add(depId);
    }
    if (isA) {
        i = val.length;
        while (i--) { _traverse(val[i], seen); }
    } else {
        keys = Object.keys(val);
        i = keys.length;
        while (i--) { _traverse(val[keys[i]], seen); }
    }
}

/*  */

var sharedPropertyDefinition = {
    enumerable: true,
    configurable: true,
    get: noop$1,
    set: noop$1
};

function proxy (target, sourceKey, key) {
    sharedPropertyDefinition.get = function proxyGetter () {
        return this[sourceKey][key]
    };
    sharedPropertyDefinition.set = function proxySetter (val) {
        this[sourceKey][key] = val;
    };
    Object.defineProperty(target, key, sharedPropertyDefinition);
}

function initState (vm) {
    vm._watchers = [];
    var opts = vm.$options;
    if (opts.props) { initProps(vm, opts.props); }
    if (opts.methods) { initMethods(vm, opts.methods); }
    if (opts.data) {
        initData(vm);
    } else {
        observe(vm._data = {}, true /* asRootData */);
    }
    if (opts.computed) { initComputed(vm, opts.computed); }
    if (opts.watch) { initWatch(vm, opts.watch); }
}

function initProps (vm, propsOptions) {
    var propsData = vm.$options.propsData || {};
    var props = vm._props = {};
    // cache prop keys so that future props updates can iterate using Array
    // instead of dynamic object key enumeration.
    var keys = vm.$options._propKeys = [];
    var isRoot = !vm.$parent;
    // root instance props should be converted
    observerState.shouldConvert = isRoot;
    var loop = function ( key ) {
        keys.push(key);
        var value = validateProp(key, propsOptions, propsData, vm);
        /* istanbul ignore else */
        {
            defineReactive$$1(props, key, value);
        }
        // static props are already proxied on the component's prototype
        // during Vue.extend(). We only need to proxy props defined at
        // instantiation here.
        if (!(key in vm)) {
            proxy(vm, "_props", key);
        }
    };

    for (var key in propsOptions) loop( key );
    observerState.shouldConvert = true;
}

function initData (vm) {
    var data = vm.$options.data;
    data = vm._data = typeof data === 'function'
        ? getData(data, vm)
        : data || {};
    if (!isPlainObject(data)) {
        data = {};
        "production" !== 'production' && warn(
            'data functions should return an object:\n' +
            'https://vuejs.org/v2/guide/components.html#data-Must-Be-a-Function',
            vm
        );
    }
    // proxy data on instance
    var keys = Object.keys(data);
    var props = vm.$options.props;
    var i = keys.length;
    while (i--) {
        if (props && hasOwn(props, keys[i])) {
            "production" !== 'production' && warn(
                "The data property \"" + (keys[i]) + "\" is already declared as a prop. " +
                "Use prop default value instead.",
                vm
            );
        } else if (!isReserved(keys[i])) {
            proxy(vm, "_data", keys[i]);
        }
    }
    // observe data
    observe(data, true /* asRootData */);
}

function getData (data, vm) {
    try {
        return data.call(vm)
    } catch (e) {
        handleError(e, vm, "data()");
        return {}
    }
}

var computedWatcherOptions = { lazy: true };

function initComputed (vm, computed) {
    var watchers = vm._computedWatchers = Object.create(null);

    for (var key in computed) {
        var userDef = computed[key];
        var getter = typeof userDef === 'function' ? userDef : userDef.get;
        watchers[key] = new Watcher(vm, getter, noop$1, computedWatcherOptions);

        // component-defined computed properties are already defined on the
        // component prototype. We only need to define computed properties defined
        // at instantiation here.
        if (!(key in vm)) {
            defineComputed(vm, key, userDef);
        } else {}
    }
}

function defineComputed (target, key, userDef) {
    if (typeof userDef === 'function') {
        sharedPropertyDefinition.get = createComputedGetter(key);
        sharedPropertyDefinition.set = noop$1;
    } else {
        sharedPropertyDefinition.get = userDef.get
            ? userDef.cache !== false
                ? createComputedGetter(key)
                : userDef.get
            : noop$1;
        sharedPropertyDefinition.set = userDef.set
            ? userDef.set
            : noop$1;
    }
    Object.defineProperty(target, key, sharedPropertyDefinition);
}

function createComputedGetter (key) {
    return function computedGetter () {
        var watcher = this._computedWatchers && this._computedWatchers[key];
        if (watcher) {
            if (watcher.dirty) {
                watcher.evaluate();
            }
            if (Dep.target) {
                watcher.depend();
            }
            return watcher.value
        }
    }
}

function initMethods (vm, methods) {
    var props = vm.$options.props;
    for (var key in methods) {
        vm[key] = methods[key] == null ? noop$1 : bind(methods[key], vm);

    }
}

function initWatch (vm, watch) {
    for (var key in watch) {
        var handler = watch[key];
        if (Array.isArray(handler)) {
            for (var i = 0; i < handler.length; i++) {
                createWatcher(vm, key, handler[i]);
            }
        } else {
            createWatcher(vm, key, handler);
        }
    }
}

function createWatcher (vm, key, handler) {
    var options;
    if (isPlainObject(handler)) {
        options = handler;
        handler = handler.handler;
    }
    if (typeof handler === 'string') {
        handler = vm[handler];
    }
    vm.$watch(key, handler, options);
}

function stateMixin (Vue) {
    // flow somehow has problems with directly declared definition object
    // when using Object.defineProperty, so we have to procedurally build up
    // the object here.
    var dataDef = {};
    dataDef.get = function () { return this._data };
    var propsDef = {};
    propsDef.get = function () { return this._props };
    Object.defineProperty(Vue.prototype, '$data', dataDef);
    Object.defineProperty(Vue.prototype, '$props', propsDef);

    Vue.prototype.$set = set;
    Vue.prototype.$delete = del;

    Vue.prototype.$watch = function (
        expOrFn,
        cb,
        options
    ) {
        var vm = this;
        options = options || {};
        options.user = true;
        var watcher = new Watcher(vm, expOrFn, cb, options);
        if (options.immediate) {
            cb.call(vm, watcher.value);
        }
        return function unwatchFn () {
            watcher.teardown();
        }
    };
}

/*  */

function initProvide (vm) {
    var provide = vm.$options.provide;
    if (provide) {
        vm._provided = typeof provide === 'function'
            ? provide.call(vm)
            : provide;
    }
}

function initInjections (vm) {
    var result = resolveInject(vm.$options.inject, vm);
    if (result) {
        Object.keys(result).forEach(function (key) {
            /* istanbul ignore else */
            {
                defineReactive$$1(vm, key, result[key]);
            }
        });
    }
}

function resolveInject (inject, vm) {
    if (inject) {
        // inject is :any because flow is not smart enough to figure out cached
        // isArray here
        var isArray = Array.isArray(inject);
        var result = Object.create(null);
        var keys = isArray
            ? inject
            : hasSymbol
                ? Reflect.ownKeys(inject)
                : Object.keys(inject);

        for (var i = 0; i < keys.length; i++) {
            var key = keys[i];
            var provideKey = isArray ? key : inject[key];
            var source = vm;
            while (source) {
                if (source._provided && provideKey in source._provided) {
                    result[key] = source._provided[provideKey];
                    break
                }
                source = source.$parent;
            }
        }
        return result
    }
}

/*  */

function createFunctionalComponent (
    Ctor,
    propsData,
    data,
    context,
    children
) {
    var props = {};
    var propOptions = Ctor.options.props;
    if (isDef(propOptions)) {
        for (var key in propOptions) {
            props[key] = validateProp(key, propOptions, propsData);
        }
    } else {
        if (isDef(data.attrs)) { mergeProps(props, data.attrs); }
        if (isDef(data.props)) { mergeProps(props, data.props); }
    }
    // ensure the createElement function in functional components
    // gets a unique context - this is necessary for correct named slot check
    var _context = Object.create(context);
    var h = function (a, b, c, d) { return createElement(_context, a, b, c, d, true); };
    var vnode = Ctor.options.render.call(null, h, {
        data: data,
        props: props,
        children: children,
        parent: context,
        listeners: data.on || {},
        injections: resolveInject(Ctor.options.inject, context),
        slots: function () { return resolveSlots(children, context); }
    });
    if (vnode instanceof VNode) {
        vnode.functionalContext = context;
        if (data.slot) {
            (vnode.data || (vnode.data = {})).slot = data.slot;
        }
    }
    return vnode
}

function mergeProps (to, from) {
    for (var key in from) {
        to[camelize(key)] = from[key];
    }
}

/*  */

// hooks to be invoked on component VNodes during patch
var componentVNodeHooks = {
    init: function init (
        vnode,
        hydrating,
        parentElm,
        refElm
    ) {
        if (!vnode.componentInstance || vnode.componentInstance._isDestroyed) {
            var child = vnode.componentInstance = createComponentInstanceForVnode(
                vnode,
                activeInstance,
                parentElm,
                refElm
            );
            child.$mount(hydrating ? vnode.elm : undefined, hydrating);
        } else if (vnode.data.keepAlive) {
            // kept-alive components, treat as a patch
            var mountedNode = vnode; // work around flow
            componentVNodeHooks.prepatch(mountedNode, mountedNode);
        }
    },

    prepatch: function prepatch (oldVnode, vnode) {
        var options = vnode.componentOptions;
        var child = vnode.componentInstance = oldVnode.componentInstance;
        updateChildComponent(
            child,
            options.propsData, // updated props
            options.listeners, // updated listeners
            vnode, // new parent vnode
            options.children // new children
        );
    },

    insert: function insert (vnode) {
        var context = vnode.context;
        var componentInstance = vnode.componentInstance;
        if (!componentInstance._isMounted) {
            componentInstance._isMounted = true;
            callHook(componentInstance, 'mounted');
        }
        if (vnode.data.keepAlive) {
            if (context._isMounted) {
                // vue-router#1212
                // During updates, a kept-alive component's child components may
                // change, so directly walking the tree here may call activated hooks
                // on incorrect children. Instead we push them into a queue which will
                // be processed after the whole patch process ended.
                queueActivatedComponent(componentInstance);
            } else {
                activateChildComponent(componentInstance, true /* direct */);
            }
        }
    },

    destroy: function destroy (vnode) {
        var componentInstance = vnode.componentInstance;
        if (!componentInstance._isDestroyed) {
            if (!vnode.data.keepAlive) {
                componentInstance.$destroy();
            } else {
                deactivateChildComponent(componentInstance, true /* direct */);
            }
        }
    }
};

var hooksToMerge = Object.keys(componentVNodeHooks);

function createComponent (
    Ctor,
    data,
    context,
    children,
    tag
) {
    if (isUndef(Ctor)) {
        return
    }

    var baseCtor = context.$options._base;

    // plain options object: turn it into a constructor
    if (isObject(Ctor)) {
        Ctor = baseCtor.extend(Ctor);
    }

    // if at this stage it's not a constructor or an async component factory,
    // reject.
    if (typeof Ctor !== 'function') {
        return
    }

    // async component
    if (isUndef(Ctor.cid)) {
        Ctor = resolveAsyncComponent(Ctor, baseCtor, context);
        if (Ctor === undefined) {
            // return nothing if this is indeed an async component
            // wait for the callback to trigger parent update.
            return
        }
    }

    // resolve constructor options in case global mixins are applied after
    // component constructor creation
    resolveConstructorOptions(Ctor);

    data = data || {};

    // transform component v-model data into props & events
    if (isDef(data.model)) {
        transformModel(Ctor.options, data);
    }

    // extract props
    var propsData = extractPropsFromVNodeData(data, Ctor, tag);

    // functional component
    if (isTrue(Ctor.options.functional)) {
        return createFunctionalComponent(Ctor, propsData, data, context, children)
    }

    // extract listeners, since these needs to be treated as
    // child component listeners instead of DOM listeners
    var listeners = data.on;
    // replace with listeners with .native modifier
    data.on = data.nativeOn;

    if (isTrue(Ctor.options.abstract)) {
        // abstract components do not keep anything
        // other than props & listeners
        data = {};
    }

    // merge component management hooks onto the placeholder node
    mergeHooks(data);

    // return a placeholder vnode
    var name = Ctor.options.name || tag;
    var vnode = new VNode(
        ("vue-component-" + (Ctor.cid) + (name ? ("-" + name) : '')),
        data, undefined, undefined, undefined, context,
        { Ctor: Ctor, propsData: propsData, listeners: listeners, tag: tag, children: children }
    );
    return vnode
}

function createComponentInstanceForVnode (
    vnode, // we know it's MountedComponentVNode but flow doesn't
    parent, // activeInstance in lifecycle state
    parentElm,
    refElm
) {
    var vnodeComponentOptions = vnode.componentOptions;
    var options = {
        _isComponent: true,
        parent: parent,
        propsData: vnodeComponentOptions.propsData,
        _componentTag: vnodeComponentOptions.tag,
        _parentVnode: vnode,
        _parentListeners: vnodeComponentOptions.listeners,
        _renderChildren: vnodeComponentOptions.children,
        _parentElm: parentElm || null,
        _refElm: refElm || null
    };
    // check inline-template render functions
    var inlineTemplate = vnode.data.inlineTemplate;
    if (isDef(inlineTemplate)) {
        options.render = inlineTemplate.render;
        options.staticRenderFns = inlineTemplate.staticRenderFns;
    }
    return new vnodeComponentOptions.Ctor(options)
}

function mergeHooks (data) {
    if (!data.hook) {
        data.hook = {};
    }
    for (var i = 0; i < hooksToMerge.length; i++) {
        var key = hooksToMerge[i];
        var fromParent = data.hook[key];
        var ours = componentVNodeHooks[key];
        data.hook[key] = fromParent ? mergeHook$1(ours, fromParent) : ours;
    }
}

function mergeHook$1 (one, two) {
    return function (a, b, c, d) {
        one(a, b, c, d);
        two(a, b, c, d);
    }
}

// transform component v-model info (value and callback) into
// prop and event handler respectively.
function transformModel (options, data) {
    var prop = (options.model && options.model.prop) || 'value';
    var event = (options.model && options.model.event) || 'input';(data.props || (data.props = {}))[prop] = data.model.value;
    var on = data.on || (data.on = {});
    if (isDef(on[event])) {
        on[event] = [data.model.callback].concat(on[event]);
    } else {
        on[event] = data.model.callback;
    }
}

/*  */

var SIMPLE_NORMALIZE = 1;
var ALWAYS_NORMALIZE = 2;

// wrapper function for providing a more flexible interface
// without getting yelled at by flow
function createElement (
    context,
    tag,
    data,
    children,
    normalizationType,
    alwaysNormalize
) {
    if (Array.isArray(data) || isPrimitive(data)) {
        normalizationType = children;
        children = data;
        data = undefined;
    }
    if (isTrue(alwaysNormalize)) {
        normalizationType = ALWAYS_NORMALIZE;
    }
    return _createElement(context, tag, data, children, normalizationType)
}

function _createElement (
    context,
    tag,
    data,
    children,
    normalizationType
) {
    if (isDef(data) && isDef((data).__ob__)) {
        "production" !== 'production' && warn(
            "Avoid using observed data object as vnode data: " + (JSON.stringify(data)) + "\n" +
            'Always create fresh vnode data objects in each render!',
            context
        );
        return createEmptyVNode()
    }
    if (!tag) {
        // in case of component :is set to falsy value
        return createEmptyVNode()
    }
    // support single function children as default scoped slot
    if (Array.isArray(children) &&
        typeof children[0] === 'function') {
        data = data || {};
        data.scopedSlots = { default: children[0] };
        children.length = 0;
    }
    if (normalizationType === ALWAYS_NORMALIZE) {
        children = normalizeChildren(children);
    } else if (normalizationType === SIMPLE_NORMALIZE) {
        children = simpleNormalizeChildren(children);
    }
    var vnode, ns;
    if (typeof tag === 'string') {
        var Ctor;
        ns = config$1.getTagNamespace(tag);
        if (config$1.isReservedTag(tag)) {
            // platform built-in elements
            vnode = new VNode(
                config$1.parsePlatformTagName(tag), data, children,
                undefined, undefined, context
            );
        } else if (isDef(Ctor = resolveAsset(context.$options, 'components', tag))) {
            // component
            vnode = createComponent(Ctor, data, context, children, tag);
        } else {
            // unknown or unlisted namespaced elements
            // check at runtime because it may get assigned a namespace when its
            // parent normalizes children
            vnode = new VNode(
                tag, data, children,
                undefined, undefined, context
            );
        }
    } else {
        // direct component options / constructor
        vnode = createComponent(tag, data, context, children);
    }
    if (vnode !== undefined) {
        if (ns) { applyNS(vnode, ns); }
        return vnode
    } else {
        return createEmptyVNode()
    }
}

function applyNS (vnode, ns) {
    vnode.ns = ns;
    if (vnode.tag === 'foreignObject') {
        // use default namespace inside foreignObject
        return
    }
    if (Array.isArray(vnode.children)) {
        for (var i = 0, l = vnode.children.length; i < l; i++) {
            var child = vnode.children[i];
            if (isDef(child.tag) && isUndef(child.ns)) {
                applyNS(child, ns);
            }
        }
    }
}

/*  */

/**
 * Runtime helper for rendering v-for lists.
 */
function renderList (
    val,
    render
) {
    var ret, i, l, keys, key;
    if (Array.isArray(val) || typeof val === 'string') {
        ret = new Array(val.length);
        for (i = 0, l = val.length; i < l; i++) {
            ret[i] = render(val[i], i);
        }
    } else if (typeof val === 'number') {
        ret = new Array(val);
        for (i = 0; i < val; i++) {
            ret[i] = render(i + 1, i);
        }
    } else if (isObject(val)) {
        keys = Object.keys(val);
        ret = new Array(keys.length);
        for (i = 0, l = keys.length; i < l; i++) {
            key = keys[i];
            ret[i] = render(val[key], key, i);
        }
    }
    return ret
}

/*  */

/**
 * Runtime helper for rendering <slot>
 */
function renderSlot (
    name,
    fallback,
    props,
    bindObject
) {
    var scopedSlotFn = this.$scopedSlots[name];
    if (scopedSlotFn) { // scoped slot
        props = props || {};
        if (bindObject) {
            extend(props, bindObject);
        }
        return scopedSlotFn(props) || fallback
    } else {
        var slotNodes = this.$slots[name];
        // warn duplicate slot usage
        if (slotNodes && "production" !== 'production') {
            slotNodes._rendered && warn(
                "Duplicate presence of slot \"" + name + "\" found in the same render tree " +
                "- this will likely cause render errors.",
                this
            );
            slotNodes._rendered = true;
        }
        return slotNodes || fallback
    }
}

/*  */

/**
 * Runtime helper for resolving filters
 */
function resolveFilter (id) {
    return resolveAsset(this.$options, 'filters', id, true) || identity
}

/*  */

/**
 * Runtime helper for checking keyCodes from config.
 */
function checkKeyCodes (
    eventKeyCode,
    key,
    builtInAlias
) {
    var keyCodes = config$1.keyCodes[key] || builtInAlias;
    if (Array.isArray(keyCodes)) {
        return keyCodes.indexOf(eventKeyCode) === -1
    } else {
        return keyCodes !== eventKeyCode
    }
}

/*  */

/**
 * Runtime helper for merging v-bind="object" into a VNode's data.
 */
function bindObjectProps (
    data,
    tag,
    value,
    asProp
) {
    if (value) {
        if (!isObject(value)) {
            "production" !== 'production' && warn(
                'v-bind without argument expects an Object or Array value',
                this
            );
        } else {
            if (Array.isArray(value)) {
                value = toObject(value);
            }
            var hash;
            for (var key in value) {
                if (key === 'class' || key === 'style') {
                    hash = data;
                } else {
                    var type = data.attrs && data.attrs.type;
                    hash = asProp || config$1.mustUseProp(tag, type, key)
                        ? data.domProps || (data.domProps = {})
                        : data.attrs || (data.attrs = {});
                }
                if (!(key in hash)) {
                    hash[key] = value[key];
                }
            }
        }
    }
    return data
}

/*  */

/**
 * Runtime helper for rendering static trees.
 */
function renderStatic (
    index,
    isInFor
) {
    var tree = this._staticTrees[index];
    // if has already-rendered static tree and not inside v-for,
    // we can reuse the same tree by doing a shallow clone.
    if (tree && !isInFor) {
        return Array.isArray(tree)
            ? cloneVNodes(tree)
            : cloneVNode(tree)
    }
    // otherwise, render a fresh tree.
    tree = this._staticTrees[index] =
        this.$options.staticRenderFns[index].call(this._renderProxy);
    markStatic(tree, ("__static__" + index), false);
    return tree
}

/**
 * Runtime helper for v-once.
 * Effectively it means marking the node as static with a unique key.
 */
function markOnce (
    tree,
    index,
    key
) {
    markStatic(tree, ("__once__" + index + (key ? ("_" + key) : "")), true);
    return tree
}

function markStatic (
    tree,
    key,
    isOnce
) {
    if (Array.isArray(tree)) {
        for (var i = 0; i < tree.length; i++) {
            if (tree[i] && typeof tree[i] !== 'string') {
                markStaticNode(tree[i], (key + "_" + i), isOnce);
            }
        }
    } else {
        markStaticNode(tree, key, isOnce);
    }
}

function markStaticNode (node, key, isOnce) {
    node.isStatic = true;
    node.key = key;
    node.isOnce = isOnce;
}

/*  */

function initRender (vm) {
    vm._vnode = null; // the root of the child tree
    vm._staticTrees = null;
    var parentVnode = vm.$vnode = vm.$options._parentVnode; // the placeholder node in parent tree
    var renderContext = parentVnode && parentVnode.context;
    vm.$slots = resolveSlots(vm.$options._renderChildren, renderContext);
    vm.$scopedSlots = emptyObject;
    // bind the createElement fn to this instance
    // so that we get proper render context inside it.
    // args order: tag, data, children, normalizationType, alwaysNormalize
    // internal version is used by render functions compiled from templates
    vm._c = function (a, b, c, d) { return createElement(vm, a, b, c, d, false); };
    // normalization is always applied for the public version, used in
    // user-written render functions.
    vm.$createElement = function (a, b, c, d) { return createElement(vm, a, b, c, d, true); };
}

function renderMixin (Vue) {
    Vue.prototype.$nextTick = function (fn) {
        return nextTick$1(fn, this)
    };

    Vue.prototype._render = function () {
        var vm = this;
        var ref = vm.$options;
        var render = ref.render;
        var staticRenderFns = ref.staticRenderFns;
        var _parentVnode = ref._parentVnode;

        if (vm._isMounted) {
            // clone slot nodes on re-renders
            for (var key in vm.$slots) {
                vm.$slots[key] = cloneVNodes(vm.$slots[key]);
            }
        }

        vm.$scopedSlots = (_parentVnode && _parentVnode.data.scopedSlots) || emptyObject;

        if (staticRenderFns && !vm._staticTrees) {
            vm._staticTrees = [];
        }
        // set parent vnode. this allows render functions to have access
        // to the data on the placeholder node.
        vm.$vnode = _parentVnode;
        // render self
        var vnode;
        try {
            vnode = render.call(vm._renderProxy, vm.$createElement);
        } catch (e) {
            handleError(e, vm, "render function");
            // return error render result,
            // or previous vnode to prevent render error causing blank component
            /* istanbul ignore else */
            {
                vnode = vm._vnode;
            }
        }
        // return empty vnode in case the render function errored out
        if (!(vnode instanceof VNode)) {
            if ("production" !== 'production' && Array.isArray(vnode)) {
                warn(
                    'Multiple root nodes returned from render function. Render function ' +
                    'should return a single root node.',
                    vm
                );
            }
            vnode = createEmptyVNode();
        }
        // set parent
        vnode.parent = _parentVnode;
        return vnode
    };

    // internal render helpers.
    // these are exposed on the instance prototype to reduce generated render
    // code size.
    Vue.prototype._o = markOnce;
    Vue.prototype._n = toNumber;
    Vue.prototype._s = _toString;
    Vue.prototype._l = renderList;
    Vue.prototype._t = renderSlot;
    Vue.prototype._q = looseEqual;
    Vue.prototype._i = looseIndexOf;
    Vue.prototype._m = renderStatic;
    Vue.prototype._f = resolveFilter;
    Vue.prototype._k = checkKeyCodes;
    Vue.prototype._b = bindObjectProps;
    Vue.prototype._v = createTextVNode;
    Vue.prototype._e = createEmptyVNode;
    Vue.prototype._u = resolveScopedSlots;
}

/*  */

var uid$1 = 0;

function initMixin (Vue) {
    Vue.prototype._init = function (options) {
        var vm = this;
        // a uid
        vm._uid = uid$1++;

        var startTag, endTag;
        /* istanbul ignore if */
        if ("production" !== 'production' && config$1.performance && mark) {
            startTag = "vue-perf-init:" + (vm._uid);
            endTag = "vue-perf-end:" + (vm._uid);
            mark(startTag);
        }

        // a flag to avoid this being observed
        vm._isVue = true;
        // merge options
        if (options && options._isComponent) {
            // optimize internal component instantiation
            // since dynamic options merging is pretty slow, and none of the
            // internal component options needs special treatment.
            initInternalComponent(vm, options);
        } else {
            vm.$options = mergeOptions(
                resolveConstructorOptions(vm.constructor),
                options || {},
                vm
            );
        }
        /* istanbul ignore else */
        {
            vm._renderProxy = vm;
        }
        // expose real self
        vm._self = vm;
        initLifecycle(vm);
        initEvents(vm);
        initRender(vm);
        callHook(vm, 'beforeCreate');
        initInjections(vm); // resolve injections before data/props
        initState(vm);
        initProvide(vm); // resolve provide after data/props
        callHook(vm, 'created');

        /* istanbul ignore if */
        if ("production" !== 'production' && config$1.performance && mark) {
            vm._name = formatComponentName(vm, false);
            mark(endTag);
            measure(((vm._name) + " init"), startTag, endTag);
        }

        if (vm.$options.el) {
            vm.$mount(vm.$options.el);
        }
    };
}

function initInternalComponent (vm, options) {
    var opts = vm.$options = Object.create(vm.constructor.options);
    // doing this because it's faster than dynamic enumeration.
    opts.parent = options.parent;
    opts.propsData = options.propsData;
    opts._parentVnode = options._parentVnode;
    opts._parentListeners = options._parentListeners;
    opts._renderChildren = options._renderChildren;
    opts._componentTag = options._componentTag;
    opts._parentElm = options._parentElm;
    opts._refElm = options._refElm;
    if (options.render) {
        opts.render = options.render;
        opts.staticRenderFns = options.staticRenderFns;
    }
}

function resolveConstructorOptions (Ctor) {
    var options = Ctor.options;
    if (Ctor.super) {
        var superOptions = resolveConstructorOptions(Ctor.super);
        var cachedSuperOptions = Ctor.superOptions;
        if (superOptions !== cachedSuperOptions) {
            // super option changed,
            // need to resolve new options.
            Ctor.superOptions = superOptions;
            // check if there are any late-modified/attached options (#4976)
            var modifiedOptions = resolveModifiedOptions(Ctor);
            // update base extend options
            if (modifiedOptions) {
                extend(Ctor.extendOptions, modifiedOptions);
            }
            options = Ctor.options = mergeOptions(superOptions, Ctor.extendOptions);
            if (options.name) {
                options.components[options.name] = Ctor;
            }
        }
    }
    return options
}

function resolveModifiedOptions (Ctor) {
    var modified;
    var latest = Ctor.options;
    var extended = Ctor.extendOptions;
    var sealed = Ctor.sealedOptions;
    for (var key in latest) {
        if (latest[key] !== sealed[key]) {
            if (!modified) { modified = {}; }
            modified[key] = dedupe(latest[key], extended[key], sealed[key]);
        }
    }
    return modified
}

function dedupe (latest, extended, sealed) {
    // compare latest and sealed to ensure lifecycle hooks won't be duplicated
    // between merges
    if (Array.isArray(latest)) {
        var res = [];
        sealed = Array.isArray(sealed) ? sealed : [sealed];
        extended = Array.isArray(extended) ? extended : [extended];
        for (var i = 0; i < latest.length; i++) {
            // push original options and not sealed options to exclude duplicated options
            if (extended.indexOf(latest[i]) >= 0 || sealed.indexOf(latest[i]) < 0) {
                res.push(latest[i]);
            }
        }
        return res
    } else {
        return latest
    }
}

function Vue$3 (options) {
    if ("production" !== 'production' &&
        !(this instanceof Vue$3)) {
        warn('Vue is a constructor and should be called with the `new` keyword');
    }
    this._init(options);
}

initMixin(Vue$3);
stateMixin(Vue$3);
eventsMixin(Vue$3);
lifecycleMixin(Vue$3);
renderMixin(Vue$3);

/*  */

function initUse (Vue) {
    Vue.use = function (plugin) {
        /* istanbul ignore if */
        if (plugin.installed) {
            return
        }
        // additional parameters
        var args = toArray(arguments, 1);
        args.unshift(this);
        if (typeof plugin.install === 'function') {
            plugin.install.apply(plugin, args);
        } else if (typeof plugin === 'function') {
            plugin.apply(null, args);
        }
        plugin.installed = true;
        return this
    };
}

/*  */

function initMixin$1 (Vue) {
    Vue.mixin = function (mixin) {
        this.options = mergeOptions(this.options, mixin);
    };
}

/*  */

function initExtend (Vue) {
    /**
     * Each instance constructor, including Vue, has a unique
     * cid. This enables us to create wrapped "child
     * constructors" for prototypal inheritance and cache them.
     */
    Vue.cid = 0;
    var cid = 1;

    /**
     * Class inheritance
     */
    Vue.extend = function (extendOptions) {
        extendOptions = extendOptions || {};
        var Super = this;
        var SuperId = Super.cid;
        var cachedCtors = extendOptions._Ctor || (extendOptions._Ctor = {});
        if (cachedCtors[SuperId]) {
            return cachedCtors[SuperId]
        }

        var name = extendOptions.name || Super.options.name;
        var Sub = function VueComponent (options) {
            this._init(options);
        };
        Sub.prototype = Object.create(Super.prototype);
        Sub.prototype.constructor = Sub;
        Sub.cid = cid++;
        Sub.options = mergeOptions(
            Super.options,
            extendOptions
        );
        Sub['super'] = Super;

        // For props and computed properties, we define the proxy getters on
        // the Vue instances at extension time, on the extended prototype. This
        // avoids Object.defineProperty calls for each instance created.
        if (Sub.options.props) {
            initProps$1(Sub);
        }
        if (Sub.options.computed) {
            initComputed$1(Sub);
        }

        // allow further extension/mixin/plugin usage
        Sub.extend = Super.extend;
        Sub.mixin = Super.mixin;
        Sub.use = Super.use;

        // create asset registers, so extended classes
        // can have their private assets too.
        ASSET_TYPES.forEach(function (type) {
            Sub[type] = Super[type];
        });
        // enable recursive self-lookup
        if (name) {
            Sub.options.components[name] = Sub;
        }

        // keep a reference to the super options at extension time.
        // later at instantiation we can check if Super's options have
        // been updated.
        Sub.superOptions = Super.options;
        Sub.extendOptions = extendOptions;
        Sub.sealedOptions = extend({}, Sub.options);

        // cache constructor
        cachedCtors[SuperId] = Sub;
        return Sub
    };
}

function initProps$1 (Comp) {
    var props = Comp.options.props;
    for (var key in props) {
        proxy(Comp.prototype, "_props", key);
    }
}

function initComputed$1 (Comp) {
    var computed = Comp.options.computed;
    for (var key in computed) {
        defineComputed(Comp.prototype, key, computed[key]);
    }
}

/*  */

function initAssetRegisters (Vue) {
    /**
     * Create asset registration methods.
     */
    ASSET_TYPES.forEach(function (type) {
        Vue[type] = function (
            id,
            definition
        ) {
            if (!definition) {
                return this.options[type + 's'][id]
            } else {
                /* istanbul ignore if */
                if (type === 'component' && isPlainObject(definition)) {
                    definition.name = definition.name || id;
                    definition = this.options._base.extend(definition);
                }
                if (type === 'directive' && typeof definition === 'function') {
                    definition = { bind: definition, update: definition };
                }
                this.options[type + 's'][id] = definition;
                return definition
            }
        };
    });
}

/*  */

var patternTypes = [String, RegExp];

function getComponentName (opts) {
    return opts && (opts.Ctor.options.name || opts.tag)
}

function matches (pattern, name) {
    if (typeof pattern === 'string') {
        return pattern.split(',').indexOf(name) > -1
    } else if (isRegExp(pattern)) {
        return pattern.test(name)
    }
    /* istanbul ignore next */
    return false
}

function pruneCache (cache, current, filter) {
    for (var key in cache) {
        var cachedNode = cache[key];
        if (cachedNode) {
            var name = getComponentName(cachedNode.componentOptions);
            if (name && !filter(name)) {
                if (cachedNode !== current) {
                    pruneCacheEntry(cachedNode);
                }
                cache[key] = null;
            }
        }
    }
}

function pruneCacheEntry (vnode) {
    if (vnode) {
        vnode.componentInstance.$destroy();
    }
}

var KeepAlive = {
    name: 'keep-alive',
    abstract: true,

    props: {
        include: patternTypes,
        exclude: patternTypes
    },

    created: function created () {
        this.cache = Object.create(null);
    },

    destroyed: function destroyed () {
        var this$1 = this;

        for (var key in this$1.cache) {
            pruneCacheEntry(this$1.cache[key]);
        }
    },

    watch: {
        include: function include (val) {
            pruneCache(this.cache, this._vnode, function (name) { return matches(val, name); });
        },
        exclude: function exclude (val) {
            pruneCache(this.cache, this._vnode, function (name) { return !matches(val, name); });
        }
    },

    render: function render () {
        var vnode = getFirstComponentChild(this.$slots.default);
        var componentOptions = vnode && vnode.componentOptions;
        if (componentOptions) {
            // check pattern
            var name = getComponentName(componentOptions);
            if (name && (
                (this.include && !matches(this.include, name)) ||
                (this.exclude && matches(this.exclude, name))
            )) {
                return vnode
            }
            var key = vnode.key == null
                // same constructor may get registered as different local components
                // so cid alone is not enough (#3269)
                ? componentOptions.Ctor.cid + (componentOptions.tag ? ("::" + (componentOptions.tag)) : '')
                : vnode.key;
            if (this.cache[key]) {
                vnode.componentInstance = this.cache[key].componentInstance;
            } else {
                this.cache[key] = vnode;
            }
            vnode.data.keepAlive = true;
        }
        return vnode
    }
};

var builtInComponents = {
    KeepAlive: KeepAlive
};

/*  */

function initGlobalAPI (Vue) {
    // config
    var configDef = {};
    configDef.get = function () { return config$1; };
    Object.defineProperty(Vue, 'config', configDef);

    // exposed util methods.
    // NOTE: these are not considered part of the public API - avoid relying on
    // them unless you are aware of the risk.
    Vue.util = {
        warn: warn,
        extend: extend,
        mergeOptions: mergeOptions,
        defineReactive: defineReactive$$1
    };

    Vue.set = set;
    Vue.delete = del;
    Vue.nextTick = nextTick$1;

    Vue.options = Object.create(null);
    ASSET_TYPES.forEach(function (type) {
        Vue.options[type + 's'] = Object.create(null);
    });

    // this is used to identify the "base" constructor to extend all plain-object
    // components with in Weex's multi-instance scenarios.
    Vue.options._base = Vue;

    extend(Vue.options.components, builtInComponents);

    initUse(Vue);
    initMixin$1(Vue);
    initExtend(Vue);
    initAssetRegisters(Vue);
}

initGlobalAPI(Vue$3);

Object.defineProperty(Vue$3.prototype, '$isServer', {
    get: isServerRendering
});

Vue$3.version = '2.3.0';

/*  */

// these are reserved for web because they are directly compiled away
// during template compilation
var isReservedAttr = makeMap('style,class');

// attributes that should be using props for binding
var acceptValue = makeMap('input,textarea,option,select');
var mustUseProp = function (tag, type, attr) {
    return (
        (attr === 'value' && acceptValue(tag)) && type !== 'button' ||
        (attr === 'selected' && tag === 'option') ||
        (attr === 'checked' && tag === 'input') ||
        (attr === 'muted' && tag === 'video')
    )
};

var isEnumeratedAttr = makeMap('contenteditable,draggable,spellcheck');

var isBooleanAttr = makeMap(
    'allowfullscreen,async,autofocus,autoplay,checked,compact,controls,declare,' +
    'default,defaultchecked,defaultmuted,defaultselected,defer,disabled,' +
    'enabled,formnovalidate,hidden,indeterminate,inert,ismap,itemscope,loop,multiple,' +
    'muted,nohref,noresize,noshade,novalidate,nowrap,open,pauseonexit,readonly,' +
    'required,reversed,scoped,seamless,selected,sortable,translate,' +
    'truespeed,typemustmatch,visible'
);

var xlinkNS = 'http://www.w3.org/1999/xlink';

var isXlink = function (name) {
    return name.charAt(5) === ':' && name.slice(0, 5) === 'xlink'
};

var getXlinkProp = function (name) {
    return isXlink(name) ? name.slice(6, name.length) : ''
};

var isFalsyAttrValue = function (val) {
    return val == null || val === false
};

/*  */

function genClassForVnode (vnode) {
    var data = vnode.data;
    var parentNode = vnode;
    var childNode = vnode;
    while (isDef(childNode.componentInstance)) {
        childNode = childNode.componentInstance._vnode;
        if (childNode.data) {
            data = mergeClassData(childNode.data, data);
        }
    }
    while (isDef(parentNode = parentNode.parent)) {
        if (parentNode.data) {
            data = mergeClassData(data, parentNode.data);
        }
    }
    return genClassFromData(data)
}

function mergeClassData (child, parent) {
    return {
        staticClass: concat(child.staticClass, parent.staticClass),
        class: isDef(child.class)
            ? [child.class, parent.class]
            : parent.class
    }
}

function genClassFromData (data) {
    var dynamicClass = data.class;
    var staticClass = data.staticClass;
    if (isDef(staticClass) || isDef(dynamicClass)) {
        return concat(staticClass, stringifyClass(dynamicClass))
    }
    /* istanbul ignore next */
    return ''
}

function concat (a, b) {
    return a ? b ? (a + ' ' + b) : a : (b || '')
}

function stringifyClass (value) {
    if (isUndef(value)) {
        return ''
    }
    if (typeof value === 'string') {
        return value
    }
    var res = '';
    if (Array.isArray(value)) {
        var stringified;
        for (var i = 0, l = value.length; i < l; i++) {
            if (isDef(value[i])) {
                if (isDef(stringified = stringifyClass(value[i])) && stringified !== '') {
                    res += stringified + ' ';
                }
            }
        }
        return res.slice(0, -1)
    }
    if (isObject(value)) {
        for (var key in value) {
            if (value[key]) { res += key + ' '; }
        }
        return res.slice(0, -1)
    }
    /* istanbul ignore next */
    return res
}

/*  */

var namespaceMap = {
    svg: 'http://www.w3.org/2000/svg',
    math: 'http://www.w3.org/1998/Math/MathML'
};

var isHTMLTag = makeMap(
    'html,body,base,head,link,meta,style,title,' +
    'address,article,aside,footer,header,h1,h2,h3,h4,h5,h6,hgroup,nav,section,' +
    'div,dd,dl,dt,figcaption,figure,hr,img,li,main,ol,p,pre,ul,' +
    'a,b,abbr,bdi,bdo,br,cite,code,data,dfn,em,i,kbd,mark,q,rp,rt,rtc,ruby,' +
    's,samp,small,span,strong,sub,sup,time,u,var,wbr,area,audio,map,track,video,' +
    'embed,object,param,source,canvas,script,noscript,del,ins,' +
    'caption,col,colgroup,table,thead,tbody,td,th,tr,' +
    'button,datalist,fieldset,form,input,label,legend,meter,optgroup,option,' +
    'output,progress,select,textarea,' +
    'details,dialog,menu,menuitem,summary,' +
    'content,element,shadow,template'
);

// this map is intentionally selective, only covering SVG elements that may
// contain child elements.
var isSVG = makeMap(
    'svg,animate,circle,clippath,cursor,defs,desc,ellipse,filter,font-face,' +
    'foreignObject,g,glyph,image,line,marker,mask,missing-glyph,path,pattern,' +
    'polygon,polyline,rect,switch,symbol,text,textpath,tspan,use,view',
    true
);

var isPreTag = function (tag) { return tag === 'pre'; };

var isReservedTag = function (tag) {
    return isHTMLTag(tag) || isSVG(tag)
};

function getTagNamespace (tag) {
    if (isSVG(tag)) {
        return 'svg'
    }
    // basic support for MathML
    // note it doesn't support other MathML elements being component roots
    if (tag === 'math') {
        return 'math'
    }
}

var unknownElementCache = Object.create(null);
function isUnknownElement (tag) {
    /* istanbul ignore if */
    if (!inBrowser) {
        return true
    }
    if (isReservedTag(tag)) {
        return false
    }
    tag = tag.toLowerCase();
    /* istanbul ignore if */
    if (unknownElementCache[tag] != null) {
        return unknownElementCache[tag]
    }
    var el = document.createElement(tag);
    if (tag.indexOf('-') > -1) {
        // http://stackoverflow.com/a/28210364/1070244
        return (unknownElementCache[tag] = (
            el.constructor === window.HTMLUnknownElement ||
            el.constructor === window.HTMLElement
        ))
    } else {
        return (unknownElementCache[tag] = /HTMLUnknownElement/.test(el.toString()))
    }
}

/*  */

/**
 * Query an element selector if it's not an element already.
 */
function query (el) {
    if (typeof el === 'string') {
        var selected = document.selectFirst(el);
        if (!selected) {
            "production" !== 'production' && warn(
                'Cannot find element: ' + el
            );
            return document.createElement('div')
        }
        return selected
    } else {
        return el
    }
}

/*  */

function createElement$1 (tagName, vnode) {
    var elm = document.createElement(tagName);
    if (tagName !== 'select') {
        return elm
    }
    // false or null will remove the attribute but undefined will not
    if (vnode.data && vnode.data.attrs && vnode.data.attrs.multiple !== undefined) {
        elm.setAttribute('multiple', 'multiple');
    }
    return elm
}

function createElementNS (namespace, tagName) {
    return document.createElementNS(namespaceMap[namespace], tagName)
}

function createTextNode (text) {
    return document.createTextNode(text)
}

function createComment (text) {
    return document.createComment(text)
}

function insertBefore (parentNode, newNode, referenceNode) {
    parentNode.insertBefore(newNode, referenceNode);
}

function removeChild (node, child) {
    node.removeChild(child);
}

function appendChild (node, child) {
    node.appendChild(child);
}

function parentNode (node) {
    return node.parentNode
}

function nextSibling (node) {
    return node.nextSibling
}

function tagName (node) {
    return node.tagName
}

function setTextContent (node, text) {
    node.textContent = text;
}

function setAttribute (node, key, val) {
    node.setAttribute(key, val);
}


var nodeOps = Object.freeze({
    createElement: createElement$1,
    createElementNS: createElementNS,
    createTextNode: createTextNode,
    createComment: createComment,
    insertBefore: insertBefore,
    removeChild: removeChild,
    appendChild: appendChild,
    parentNode: parentNode,
    nextSibling: nextSibling,
    tagName: tagName,
    setTextContent: setTextContent,
    setAttribute: setAttribute
});

/*  */

var ref = {
    create: function create (_, vnode) {
        registerRef(vnode);
    },
    update: function update (oldVnode, vnode) {
        if (oldVnode.data.ref !== vnode.data.ref) {
            registerRef(oldVnode, true);
            registerRef(vnode);
        }
    },
    destroy: function destroy (vnode) {
        registerRef(vnode, true);
    }
};

function registerRef (vnode, isRemoval) {
    var key = vnode.data.ref;
    if (!key) { return }

    var vm = vnode.context;
    var ref = vnode.componentInstance || vnode.elm;
    var refs = vm.$refs;
    if (isRemoval) {
        if (Array.isArray(refs[key])) {
            remove(refs[key], ref);
        } else if (refs[key] === ref) {
            refs[key] = undefined;
        }
    } else {
        if (vnode.data.refInFor) {
            if (Array.isArray(refs[key]) && refs[key].indexOf(ref) < 0) {
                refs[key].push(ref);
            } else {
                refs[key] = [ref];
            }
        } else {
            refs[key] = ref;
        }
    }
}

/**
 * Virtual DOM patching algorithm based on Snabbdom by
 * Simon Friis Vindum (@paldepind)
 * Licensed under the MIT License
 * https://github.com/paldepind/snabbdom/blob/master/LICENSE
 *
 * modified by Evan You (@yyx990803)
 *

 /*
 * Not type-checking this because this file is perf-critical and the cost
 * of making flow understand it is not worth it.
 */

var emptyNode = new VNode('', {}, []);

var hooks = ['create', 'activate', 'update', 'remove', 'destroy'];

function sameVnode (a, b) {
    return (
        a.key === b.key &&
        a.tag === b.tag &&
        a.isComment === b.isComment &&
        isDef(a.data) === isDef(b.data) &&
        sameInputType(a, b)
    )
}

// Some browsers do not support dynamically changing type for <input>
// so they need to be treated as different nodes
function sameInputType (a, b) {
    if (a.tag !== 'input') { return true }
    var i;
    var typeA = isDef(i = a.data) && isDef(i = i.attrs) && i.type;
    var typeB = isDef(i = b.data) && isDef(i = i.attrs) && i.type;
    return typeA === typeB
}

function createKeyToOldIdx (children, beginIdx, endIdx) {
    var i, key;
    var map = {};
    for (i = beginIdx; i <= endIdx; ++i) {
        key = children[i].key;
        if (isDef(key)) { map[key] = i; }
    }
    return map
}

function createPatchFunction (backend) {
    var i, j;
    var cbs = {};

    var modules = backend.modules;
    var nodeOps = backend.nodeOps;

    for (i = 0; i < hooks.length; ++i) {
        cbs[hooks[i]] = [];
        for (j = 0; j < modules.length; ++j) {
            if (isDef(modules[j][hooks[i]])) {
                cbs[hooks[i]].push(modules[j][hooks[i]]);
            }
        }
    }

    function emptyNodeAt (elm) {
        return new VNode(nodeOps.tagName(elm).toLowerCase(), {}, [], undefined, elm)
    }

    function createRmCb (childElm, listeners) {
        function remove$$1 () {
            if (--remove$$1.listeners === 0) {
                removeNode(childElm);
            }
        }
        remove$$1.listeners = listeners;
        return remove$$1
    }

    function removeNode (el) {
        var parent = nodeOps.parentNode(el);
        // element may have already been removed due to v-html / v-text
        if (isDef(parent)) {
            nodeOps.removeChild(parent, el);
        }
    }

    var inPre = 0;
    function createElm (vnode, insertedVnodeQueue, parentElm, refElm, nested) {
        vnode.isRootInsert = !nested; // for transition enter check
        if (createComponent(vnode, insertedVnodeQueue, parentElm, refElm)) {
            return
        }

        var data = vnode.data;
        var children = vnode.children;
        var tag = vnode.tag;
        if (isDef(tag)) {
            vnode.elm = vnode.ns
                ? nodeOps.createElementNS(vnode.ns, tag)
                : nodeOps.createElement(tag, vnode);
            setScope(vnode);

            /* istanbul ignore if */
            {
                createChildren(vnode, children, insertedVnodeQueue);
                if (isDef(data)) {
                    invokeCreateHooks(vnode, insertedVnodeQueue);
                }
                insert(parentElm, vnode.elm, refElm);
            }

            if ("production" !== 'production' && data && data.pre) {
                inPre--;
            }
        } else if (isTrue(vnode.isComment)) {
            vnode.elm = nodeOps.createComment(vnode.text);
            insert(parentElm, vnode.elm, refElm);
        } else {
            vnode.elm = nodeOps.createTextNode(vnode.text);
            insert(parentElm, vnode.elm, refElm);
        }
    }

    function createComponent (vnode, insertedVnodeQueue, parentElm, refElm) {
        var i = vnode.data;
        if (isDef(i)) {
            var isReactivated = isDef(vnode.componentInstance) && i.keepAlive;
            if (isDef(i = i.hook) && isDef(i = i.init)) {
                i(vnode, false /* hydrating */, parentElm, refElm);
            }
            // after calling the init hook, if the vnode is a child component
            // it should've created a child instance and mounted it. the child
            // component also has set the placeholder vnode's elm.
            // in that case we can just return the element and be done.
            if (isDef(vnode.componentInstance)) {
                initComponent(vnode, insertedVnodeQueue);
                if (isTrue(isReactivated)) {
                    reactivateComponent(vnode, insertedVnodeQueue, parentElm, refElm);
                }
                return true
            }
        }
    }

    function initComponent (vnode, insertedVnodeQueue) {
        if (isDef(vnode.data.pendingInsert)) {
            insertedVnodeQueue.push.apply(insertedVnodeQueue, vnode.data.pendingInsert);
        }
        vnode.elm = vnode.componentInstance.$el;
        if (isPatchable(vnode)) {
            invokeCreateHooks(vnode, insertedVnodeQueue);
            setScope(vnode);
        } else {
            // empty component root.
            // skip all element-related modules except for ref (#3455)
            registerRef(vnode);
            // make sure to invoke the insert hook
            insertedVnodeQueue.push(vnode);
        }
    }

    function reactivateComponent (vnode, insertedVnodeQueue, parentElm, refElm) {
        var i;
        // hack for #4339: a reactivated component with inner transition
        // does not trigger because the inner node's created hooks are not called
        // again. It's not ideal to involve module-specific logic in here but
        // there doesn't seem to be a better way to do it.
        var innerNode = vnode;
        while (innerNode.componentInstance) {
            innerNode = innerNode.componentInstance._vnode;
            if (isDef(i = innerNode.data) && isDef(i = i.transition)) {
                for (i = 0; i < cbs.activate.length; ++i) {
                    cbs.activate[i](emptyNode, innerNode);
                }
                insertedVnodeQueue.push(innerNode);
                break
            }
        }
        // unlike a newly created component,
        // a reactivated keep-alive component doesn't insert itself
        insert(parentElm, vnode.elm, refElm);
    }

    function insert (parent, elm, ref) {
        if (isDef(parent)) {
            if (isDef(ref)) {
                if (ref.parentNode === parent) {
                    nodeOps.insertBefore(parent, elm, ref);
                }
            } else {
                nodeOps.appendChild(parent, elm);
            }
        }
    }

    function createChildren (vnode, children, insertedVnodeQueue) {
        if (Array.isArray(children)) {
            for (var i = 0; i < children.length; ++i) {
                createElm(children[i], insertedVnodeQueue, vnode.elm, null, true);
            }
        } else if (isPrimitive(vnode.text)) {
            nodeOps.appendChild(vnode.elm, nodeOps.createTextNode(vnode.text));
        }
    }

    function isPatchable (vnode) {
        while (vnode.componentInstance) {
            vnode = vnode.componentInstance._vnode;
        }
        return isDef(vnode.tag)
    }

    function invokeCreateHooks (vnode, insertedVnodeQueue) {
        for (var i$1 = 0; i$1 < cbs.create.length; ++i$1) {
            cbs.create[i$1](emptyNode, vnode);
        }
        i = vnode.data.hook; // Reuse variable
        if (isDef(i)) {
            if (isDef(i.create)) { i.create(emptyNode, vnode); }
            if (isDef(i.insert)) { insertedVnodeQueue.push(vnode); }
        }
    }

    // set scope id attribute for scoped CSS.
    // this is implemented as a special case to avoid the overhead
    // of going through the normal attribute patching process.
    function setScope (vnode) {
        var i;
        var ancestor = vnode;
        while (ancestor) {
            if (isDef(i = ancestor.context) && isDef(i = i.$options._scopeId)) {
                nodeOps.setAttribute(vnode.elm, i, '');
            }
            ancestor = ancestor.parent;
        }
        // for slot content they should also get the scopeId from the host instance.
        if (isDef(i = activeInstance) &&
            i !== vnode.context &&
            isDef(i = i.$options._scopeId)) {
            nodeOps.setAttribute(vnode.elm, i, '');
        }
    }

    function addVnodes (parentElm, refElm, vnodes, startIdx, endIdx, insertedVnodeQueue) {
        for (; startIdx <= endIdx; ++startIdx) {
            createElm(vnodes[startIdx], insertedVnodeQueue, parentElm, refElm);
        }
    }

    function invokeDestroyHook (vnode) {
        var i, j;
        var data = vnode.data;
        if (isDef(data)) {
            if (isDef(i = data.hook) && isDef(i = i.destroy)) { i(vnode); }
            for (i = 0; i < cbs.destroy.length; ++i) { cbs.destroy[i](vnode); }
        }
        if (isDef(i = vnode.children)) {
            for (j = 0; j < vnode.children.length; ++j) {
                invokeDestroyHook(vnode.children[j]);
            }
        }
    }

    function removeVnodes (parentElm, vnodes, startIdx, endIdx) {
        for (; startIdx <= endIdx; ++startIdx) {
            var ch = vnodes[startIdx];
            if (isDef(ch)) {
                if (isDef(ch.tag)) {
                    removeAndInvokeRemoveHook(ch);
                    invokeDestroyHook(ch);
                } else { // Text node
                    removeNode(ch.elm);
                }
            }
        }
    }

    function removeAndInvokeRemoveHook (vnode, rm) {
        if (isDef(rm) || isDef(vnode.data)) {
            var i;
            var listeners = cbs.remove.length + 1;
            if (isDef(rm)) {
                // we have a recursively passed down rm callback
                // increase the listeners count
                rm.listeners += listeners;
            } else {
                // directly removing
                rm = createRmCb(vnode.elm, listeners);
            }
            // recursively invoke hooks on child component root node
            if (isDef(i = vnode.componentInstance) && isDef(i = i._vnode) && isDef(i.data)) {
                removeAndInvokeRemoveHook(i, rm);
            }
            for (i = 0; i < cbs.remove.length; ++i) {
                cbs.remove[i](vnode, rm);
            }
            if (isDef(i = vnode.data.hook) && isDef(i = i.remove)) {
                i(vnode, rm);
            } else {
                rm();
            }
        } else {
            removeNode(vnode.elm);
        }
    }

    function updateChildren (parentElm, oldCh, newCh, insertedVnodeQueue, removeOnly) {
        var oldStartIdx = 0;
        var newStartIdx = 0;
        var oldEndIdx = oldCh.length - 1;
        var oldStartVnode = oldCh[0];
        var oldEndVnode = oldCh[oldEndIdx];
        var newEndIdx = newCh.length - 1;
        var newStartVnode = newCh[0];
        var newEndVnode = newCh[newEndIdx];
        var oldKeyToIdx, idxInOld, elmToMove, refElm;

        // removeOnly is a special flag used only by <transition-group>
        // to ensure removed elements stay in correct relative positions
        // during leaving transitions
        var canMove = !removeOnly;

        while (oldStartIdx <= oldEndIdx && newStartIdx <= newEndIdx) {
            if (isUndef(oldStartVnode)) {
                oldStartVnode = oldCh[++oldStartIdx]; // Vnode has been moved left
            } else if (isUndef(oldEndVnode)) {
                oldEndVnode = oldCh[--oldEndIdx];
            } else if (sameVnode(oldStartVnode, newStartVnode)) {
                patchVnode(oldStartVnode, newStartVnode, insertedVnodeQueue);
                oldStartVnode = oldCh[++oldStartIdx];
                newStartVnode = newCh[++newStartIdx];
            } else if (sameVnode(oldEndVnode, newEndVnode)) {
                patchVnode(oldEndVnode, newEndVnode, insertedVnodeQueue);
                oldEndVnode = oldCh[--oldEndIdx];
                newEndVnode = newCh[--newEndIdx];
            } else if (sameVnode(oldStartVnode, newEndVnode)) { // Vnode moved right
                patchVnode(oldStartVnode, newEndVnode, insertedVnodeQueue);
                canMove && nodeOps.insertBefore(parentElm, oldStartVnode.elm, nodeOps.nextSibling(oldEndVnode.elm));
                oldStartVnode = oldCh[++oldStartIdx];
                newEndVnode = newCh[--newEndIdx];
            } else if (sameVnode(oldEndVnode, newStartVnode)) { // Vnode moved left
                patchVnode(oldEndVnode, newStartVnode, insertedVnodeQueue);
                canMove && nodeOps.insertBefore(parentElm, oldEndVnode.elm, oldStartVnode.elm);
                oldEndVnode = oldCh[--oldEndIdx];
                newStartVnode = newCh[++newStartIdx];
            } else {
                if (isUndef(oldKeyToIdx)) { oldKeyToIdx = createKeyToOldIdx(oldCh, oldStartIdx, oldEndIdx); }
                idxInOld = isDef(newStartVnode.key) ? oldKeyToIdx[newStartVnode.key] : null;
                if (isUndef(idxInOld)) { // New element
                    createElm(newStartVnode, insertedVnodeQueue, parentElm, oldStartVnode.elm);
                    newStartVnode = newCh[++newStartIdx];
                } else {
                    elmToMove = oldCh[idxInOld];
                    /* istanbul ignore if */
                    if ("production" !== 'production' && !elmToMove) {
                        warn(
                            'It seems there are duplicate keys that is causing an update error. ' +
                            'Make sure each v-for item has a unique key.'
                        );
                    }
                    if (sameVnode(elmToMove, newStartVnode)) {
                        patchVnode(elmToMove, newStartVnode, insertedVnodeQueue);
                        oldCh[idxInOld] = undefined;
                        canMove && nodeOps.insertBefore(parentElm, newStartVnode.elm, oldStartVnode.elm);
                        newStartVnode = newCh[++newStartIdx];
                    } else {
                        // same key but different element. treat as new element
                        createElm(newStartVnode, insertedVnodeQueue, parentElm, oldStartVnode.elm);
                        newStartVnode = newCh[++newStartIdx];
                    }
                }
            }
        }
        if (oldStartIdx > oldEndIdx) {
            refElm = isUndef(newCh[newEndIdx + 1]) ? null : newCh[newEndIdx + 1].elm;
            addVnodes(parentElm, refElm, newCh, newStartIdx, newEndIdx, insertedVnodeQueue);
        } else if (newStartIdx > newEndIdx) {
            removeVnodes(parentElm, oldCh, oldStartIdx, oldEndIdx);
        }
    }

    function patchVnode (oldVnode, vnode, insertedVnodeQueue, removeOnly) {
        if (oldVnode === vnode) {
            return
        }
        // reuse element for static trees.
        // note we only do this if the vnode is cloned -
        // if the new node is not cloned it means the render functions have been
        // reset by the hot-reload-api and we need to do a proper re-render.
        if (isTrue(vnode.isStatic) &&
            isTrue(oldVnode.isStatic) &&
            vnode.key === oldVnode.key &&
            (isTrue(vnode.isCloned) || isTrue(vnode.isOnce))) {
            vnode.elm = oldVnode.elm;
            vnode.componentInstance = oldVnode.componentInstance;
            return
        }
        var i;
        var data = vnode.data;
        if (isDef(data) && isDef(i = data.hook) && isDef(i = i.prepatch)) {
            i(oldVnode, vnode);
        }
        var elm = vnode.elm = oldVnode.elm;
        var oldCh = oldVnode.children;
        var ch = vnode.children;
        if (isDef(data) && isPatchable(vnode)) {
            for (i = 0; i < cbs.update.length; ++i) { cbs.update[i](oldVnode, vnode); }
            if (isDef(i = data.hook) && isDef(i = i.update)) { i(oldVnode, vnode); }
        }
        if (isUndef(vnode.text)) {
            if (isDef(oldCh) && isDef(ch)) {
                if (oldCh !== ch) { updateChildren(elm, oldCh, ch, insertedVnodeQueue, removeOnly); }
            } else if (isDef(ch)) {
                if (isDef(oldVnode.text)) { nodeOps.setTextContent(elm, ''); }
                addVnodes(elm, null, ch, 0, ch.length - 1, insertedVnodeQueue);
            } else if (isDef(oldCh)) {
                removeVnodes(elm, oldCh, 0, oldCh.length - 1);
            } else if (isDef(oldVnode.text)) {
                nodeOps.setTextContent(elm, '');
            }
        } else if (oldVnode.text !== vnode.text) {
            nodeOps.setTextContent(elm, vnode.text);
        }
        if (isDef(data)) {
            if (isDef(i = data.hook) && isDef(i = i.postpatch)) { i(oldVnode, vnode); }
        }
    }

    function invokeInsertHook (vnode, queue, initial) {
        // delay insert hooks for component root nodes, invoke them after the
        // element is really inserted
        if (isTrue(initial) && isDef(vnode.parent)) {
            vnode.parent.data.pendingInsert = queue;
        } else {
            for (var i = 0; i < queue.length; ++i) {
                queue[i].data.hook.insert(queue[i]);
            }
        }
    }

    var bailed = false;
    // list of modules that can skip create hook during hydration because they
    // are already rendered on the client or has no need for initialization
    var isRenderedModule = makeMap('attrs,style,class,staticClass,staticStyle,key');

    // Note: this is a browser-only function so we can assume elms are DOM nodes.
    function hydrate (elm, vnode, insertedVnodeQueue) {
        vnode.elm = elm;
        var tag = vnode.tag;
        var data = vnode.data;
        var children = vnode.children;
        if (isDef(data)) {
            if (isDef(i = data.hook) && isDef(i = i.init)) { i(vnode, true /* hydrating */); }
            if (isDef(i = vnode.componentInstance)) {
                // child component. it should have hydrated its own tree.
                initComponent(vnode, insertedVnodeQueue);
                return true
            }
        }
        if (isDef(tag)) {
            if (isDef(children)) {
                // empty element, allow client to pick up and populate children
                if (!elm.hasChildNodes()) {
                    createChildren(vnode, children, insertedVnodeQueue);
                } else {
                    var childrenMatch = true;
                    var childNode = elm.firstChild;
                    for (var i$1 = 0; i$1 < children.length; i$1++) {
                        if (!childNode || !hydrate(childNode, children[i$1], insertedVnodeQueue)) {
                            childrenMatch = false;
                            break
                        }
                        childNode = childNode.nextSibling;
                    }
                    // if childNode is not null, it means the actual childNodes list is
                    // longer than the virtual children list.
                    if (!childrenMatch || childNode) {
                        if ("production" !== 'production' &&
                            typeof console !== 'undefined' &&
                            !bailed) {
                            bailed = true;
                            console.warn('Parent: ', elm);
                            console.warn('Mismatching childNodes vs. VNodes: ', elm.childNodes, children);
                        }
                        return false
                    }
                }
            }
            if (isDef(data)) {
                for (var key in data) {
                    if (!isRenderedModule(key)) {
                        invokeCreateHooks(vnode, insertedVnodeQueue);
                        break
                    }
                }
            }
        } else if (elm.data !== vnode.text) {
            elm.data = vnode.text;
        }
        return true
    }

    return function patch (oldVnode, vnode, hydrating, removeOnly, parentElm, refElm) {
        if (isUndef(vnode)) {
            if (isDef(oldVnode)) { invokeDestroyHook(oldVnode); }
            return
        }

        var isInitialPatch = false;
        var insertedVnodeQueue = [];

        if (isUndef(oldVnode)) {
            // empty mount (likely as component), create new root element
            isInitialPatch = true;
            createElm(vnode, insertedVnodeQueue, parentElm, refElm);
        } else {
            var isRealElement = isDef(oldVnode.nodeType);
            if (!isRealElement && sameVnode(oldVnode, vnode)) {
                // patch existing root node
                patchVnode(oldVnode, vnode, insertedVnodeQueue, removeOnly);
            } else {
                if (isRealElement) {
                    // mounting to a real element
                    // check if this is server-rendered content and if we can perform
                    // a successful hydration.
                    if (oldVnode.nodeType === 1 && oldVnode.hasAttribute(SSR_ATTR)) {
                        oldVnode.removeAttribute(SSR_ATTR);
                        hydrating = true;
                    }
                    if (isTrue(hydrating)) {
                        if (hydrate(oldVnode, vnode, insertedVnodeQueue)) {
                            invokeInsertHook(vnode, insertedVnodeQueue, true);
                            return oldVnode
                        } else {}
                    }
                    // either not server-rendered, or hydration failed.
                    // create an empty node and replace it
                    oldVnode = emptyNodeAt(oldVnode);
                }
                // replacing existing element
                var oldElm = oldVnode.elm;
                var parentElm$1 = nodeOps.parentNode(oldElm);
                createElm(
                    vnode,
                    insertedVnodeQueue,
                    // extremely rare edge case: do not insert if old element is in a
                    // leaving transition. Only happens when combining transition +
                    // keep-alive + HOCs. (#4590)
                    oldElm._leaveCb ? null : parentElm$1,
                    nodeOps.nextSibling(oldElm)
                );

                if (isDef(vnode.parent)) {
                    // component root element replaced.
                    // update parent placeholder node element, recursively
                    var ancestor = vnode.parent;
                    while (ancestor) {
                        ancestor.elm = vnode.elm;
                        ancestor = ancestor.parent;
                    }
                    if (isPatchable(vnode)) {
                        for (var i = 0; i < cbs.create.length; ++i) {
                            cbs.create[i](emptyNode, vnode.parent);
                        }
                    }
                }

                if (isDef(parentElm$1)) {
                    removeVnodes(parentElm$1, [oldVnode], 0, 0);
                } else if (isDef(oldVnode.tag)) {
                    invokeDestroyHook(oldVnode);
                }
            }
        }

        invokeInsertHook(vnode, insertedVnodeQueue, isInitialPatch);
        return vnode.elm
    }
}

/*  */

var directives = {
    create: updateDirectives,
    update: updateDirectives,
    destroy: function unbindDirectives (vnode) {
        updateDirectives(vnode, emptyNode);
    }
};

function updateDirectives (oldVnode, vnode) {
    if (oldVnode.data.directives || vnode.data.directives) {
        _update(oldVnode, vnode);
    }
}

function _update (oldVnode, vnode) {
    var isCreate = oldVnode === emptyNode;
    var isDestroy = vnode === emptyNode;
    var oldDirs = normalizeDirectives$1(oldVnode.data.directives, oldVnode.context);
    var newDirs = normalizeDirectives$1(vnode.data.directives, vnode.context);

    var dirsWithInsert = [];
    var dirsWithPostpatch = [];

    var key, oldDir, dir;
    for (key in newDirs) {
        oldDir = oldDirs[key];
        dir = newDirs[key];
        if (!oldDir) {
            // new directive, bind
            callHook$1(dir, 'bind', vnode, oldVnode);
            if (dir.def && dir.def.inserted) {
                dirsWithInsert.push(dir);
            }
        } else {
            // existing directive, update
            dir.oldValue = oldDir.value;
            callHook$1(dir, 'update', vnode, oldVnode);
            if (dir.def && dir.def.componentUpdated) {
                dirsWithPostpatch.push(dir);
            }
        }
    }

    if (dirsWithInsert.length) {
        var callInsert = function () {
            for (var i = 0; i < dirsWithInsert.length; i++) {
                callHook$1(dirsWithInsert[i], 'inserted', vnode, oldVnode);
            }
        };
        if (isCreate) {
            mergeVNodeHook(vnode.data.hook || (vnode.data.hook = {}), 'insert', callInsert);
        } else {
            callInsert();
        }
    }

    if (dirsWithPostpatch.length) {
        mergeVNodeHook(vnode.data.hook || (vnode.data.hook = {}), 'postpatch', function () {
            for (var i = 0; i < dirsWithPostpatch.length; i++) {
                callHook$1(dirsWithPostpatch[i], 'componentUpdated', vnode, oldVnode);
            }
        });
    }

    if (!isCreate) {
        for (key in oldDirs) {
            if (!newDirs[key]) {
                // no longer present, unbind
                callHook$1(oldDirs[key], 'unbind', oldVnode, oldVnode, isDestroy);
            }
        }
    }
}

var emptyModifiers = Object.create(null);

function normalizeDirectives$1 (
    dirs,
    vm
) {
    var res = Object.create(null);
    if (!dirs) {
        return res
    }
    var i, dir;
    for (i = 0; i < dirs.length; i++) {
        dir = dirs[i];
        if (!dir.modifiers) {
            dir.modifiers = emptyModifiers;
        }
        res[getRawDirName(dir)] = dir;
        dir.def = resolveAsset(vm.$options, 'directives', dir.name, true);
    }
    return res
}

function getRawDirName (dir) {
    return dir.rawName || ((dir.name) + "." + (Object.keys(dir.modifiers || {}).join('.')))
}

function callHook$1 (dir, hook, vnode, oldVnode, isDestroy) {
    var fn = dir.def && dir.def[hook];
    if (fn) {
        try {
            fn(vnode.elm, dir, vnode, oldVnode, isDestroy);
        } catch (e) {
            handleError(e, vnode.context, ("directive " + (dir.name) + " " + hook + " hook"));
        }
    }
}

var baseModules = [
    ref,
    directives
];

/*  */

function updateAttrs (oldVnode, vnode) {
    if (isUndef(oldVnode.data.attrs) && isUndef(vnode.data.attrs)) {
        return
    }
    var key, cur, old;
    var elm = vnode.elm;
    var oldAttrs = oldVnode.data.attrs || {};
    var attrs = vnode.data.attrs || {};
    // clone observed objects, as the user probably wants to mutate it
    if (isDef(attrs.__ob__)) {
        attrs = vnode.data.attrs = extend({}, attrs);
    }

    for (key in attrs) {
        cur = attrs[key];
        old = oldAttrs[key];
        if (old !== cur) {
            setAttr(elm, key, cur);
        }
    }
    // #4391: in IE9, setting type can reset value for input[type=radio]
    /* istanbul ignore if */
    if (isIE9 && attrs.value !== oldAttrs.value) {
        setAttr(elm, 'value', attrs.value);
    }
    for (key in oldAttrs) {
        if (isUndef(attrs[key])) {
            if (isXlink(key)) {
                elm.removeAttributeNS(xlinkNS, getXlinkProp(key));
            } else if (!isEnumeratedAttr(key)) {
                elm.removeAttribute(key);
            }
        }
    }
}

function setAttr (el, key, value) {
    if (isBooleanAttr(key)) {
        // set attribute for blank value
        // e.g. <option disabled>Select one</option>
        if (isFalsyAttrValue(value)) {
            el.removeAttribute(key);
        } else {
            el.setAttribute(key, key);
        }
    } else if (isEnumeratedAttr(key)) {
        el.setAttribute(key, isFalsyAttrValue(value) || value === 'false' ? 'false' : 'true');
    } else if (isXlink(key)) {
        if (isFalsyAttrValue(value)) {
            el.removeAttributeNS(xlinkNS, getXlinkProp(key));
        } else {
            el.setAttributeNS(xlinkNS, key, value);
        }
    } else {
        if (isFalsyAttrValue(value)) {
            el.removeAttribute(key);
        } else {
            el.setAttribute(key, value);
        }
    }
}

var attrs = {
    create: updateAttrs,
    update: updateAttrs
};

/*  */

function updateClass (oldVnode, vnode) {
    var el = vnode.elm;
    var data = vnode.data;
    var oldData = oldVnode.data;
    if (
        isUndef(data.staticClass) &&
        isUndef(data.class) && (
            isUndef(oldData) || (
                isUndef(oldData.staticClass) &&
                isUndef(oldData.class)
            )
        )
    ) {
        return
    }

    var cls = genClassForVnode(vnode);

    // handle transition classes
    var transitionClass = el._transitionClasses;
    if (isDef(transitionClass)) {
        cls = concat(cls, stringifyClass(transitionClass));
    }

    // set the class
    if (cls !== el._prevClass) {
        el.setAttribute('class', cls);
        el._prevClass = cls;
    }
}

var klass = {
    create: updateClass,
    update: updateClass
};

/*  */

var validDivisionCharRE = /[\w).+\-_$\]]/;

function parseFilters (exp) {
    var inSingle = false;
    var inDouble = false;
    var inTemplateString = false;
    var inRegex = false;
    var curly = 0;
    var square = 0;
    var paren = 0;
    var lastFilterIndex = 0;
    var c, prev, i, expression, filters;

    for (i = 0; i < exp.length; i++) {
        prev = c;
        c = exp.charCodeAt(i);
        if (inSingle) {
            if (c === 0x27 && prev !== 0x5C) { inSingle = false; }
        } else if (inDouble) {
            if (c === 0x22 && prev !== 0x5C) { inDouble = false; }
        } else if (inTemplateString) {
            if (c === 0x60 && prev !== 0x5C) { inTemplateString = false; }
        } else if (inRegex) {
            if (c === 0x2f && prev !== 0x5C) { inRegex = false; }
        } else if (
            c === 0x7C && // pipe
            exp.charCodeAt(i + 1) !== 0x7C &&
            exp.charCodeAt(i - 1) !== 0x7C &&
            !curly && !square && !paren
        ) {
            if (expression === undefined) {
                // first filter, end of expression
                lastFilterIndex = i + 1;
                expression = exp.slice(0, i).trim();
            } else {
                pushFilter();
            }
        } else {
            switch (c) {
                case 0x22: inDouble = true; break         // "
                case 0x27: inSingle = true; break         // '
                case 0x60: inTemplateString = true; break // `
                case 0x28: paren++; break                 // (
                case 0x29: paren--; break                 // )
                case 0x5B: square++; break                // [
                case 0x5D: square--; break                // ]
                case 0x7B: curly++; break                 // {
                case 0x7D: curly--; break                 // }
            }
            if (c === 0x2f) { // /
                var j = i - 1;
                var p = (void 0);
                // find first non-whitespace prev char
                for (; j >= 0; j--) {
                    p = exp.charAt(j);
                    if (p !== ' ') { break }
                }
                if (!p || !validDivisionCharRE.test(p)) {
                    inRegex = true;
                }
            }
        }
    }

    if (expression === undefined) {
        expression = exp.slice(0, i).trim();
    } else if (lastFilterIndex !== 0) {
        pushFilter();
    }

    function pushFilter () {
        (filters || (filters = [])).push(exp.slice(lastFilterIndex, i).trim());
        lastFilterIndex = i + 1;
    }

    if (filters) {
        for (i = 0; i < filters.length; i++) {
            expression = wrapFilter(expression, filters[i]);
        }
    }

    return expression
}

function wrapFilter (exp, filter) {
    var i = filter.indexOf('(');
    if (i < 0) {
        // _f: resolveFilter
        return ("_f(\"" + filter + "\")(" + exp + ")")
    } else {
        var name = filter.slice(0, i);
        var args = filter.slice(i + 1);
        return ("_f(\"" + name + "\")(" + exp + "," + args)
    }
}

/*  */

function baseWarn (msg) {
    console.error(("[Vue compiler]: " + msg));
}

function pluckModuleFunction (
    modules,
    key
) {
    return modules
        ? modules.map(function (m) { return m[key]; }).filter(function (_) { return _; })
        : []
}

function addProp (el, name, value) {
    (el.props || (el.props = [])).push({ name: name, value: value });
}

function addAttr (el, name, value) {
    (el.attrs || (el.attrs = [])).push({ name: name, value: value });
}

function addDirective (
    el,
    name,
    rawName,
    value,
    arg,
    modifiers
) {
    (el.directives || (el.directives = [])).push({ name: name, rawName: rawName, value: value, arg: arg, modifiers: modifiers });
}

function addHandler (
    el,
    name,
    value,
    modifiers,
    important,
    warn
) {
    // warn prevent and passive modifier
    /* istanbul ignore if */
    if (
        "production" !== 'production' && warn &&
        modifiers && modifiers.prevent && modifiers.passive
    ) {
        warn(
            'passive and prevent can\'t be used together. ' +
            'Passive handler can\'t prevent default event.'
        );
    }
    // check capture modifier
    if (modifiers && modifiers.capture) {
        delete modifiers.capture;
        name = '!' + name; // mark the event as captured
    }
    if (modifiers && modifiers.once) {
        delete modifiers.once;
        name = '~' + name; // mark the event as once
    }
    /* istanbul ignore if */
    if (modifiers && modifiers.passive) {
        delete modifiers.passive;
        name = '&' + name; // mark the event as passive
    }
    var events;
    if (modifiers && modifiers.native) {
        delete modifiers.native;
        events = el.nativeEvents || (el.nativeEvents = {});
    } else {
        events = el.events || (el.events = {});
    }
    var newHandler = { value: value, modifiers: modifiers };
    var handlers = events[name];
    /* istanbul ignore if */
    if (Array.isArray(handlers)) {
        important ? handlers.unshift(newHandler) : handlers.push(newHandler);
    } else if (handlers) {
        events[name] = important ? [newHandler, handlers] : [handlers, newHandler];
    } else {
        events[name] = newHandler;
    }
}

function getBindingAttr (
    el,
    name,
    getStatic
) {
    var dynamicValue =
        getAndRemoveAttr(el, ':' + name) ||
        getAndRemoveAttr(el, 'v-bind:' + name);
    if (dynamicValue != null) {
        return parseFilters(dynamicValue)
    } else if (getStatic !== false) {
        var staticValue = getAndRemoveAttr(el, name);
        if (staticValue != null) {
            return JSON.stringify(staticValue)
        }
    }
}

function getAndRemoveAttr (el, name) {
    var val;
    if ((val = el.attrsMap[name]) != null) {
        var list = el.attrsList;
        for (var i = 0, l = list.length; i < l; i++) {
            if (list[i].name === name) {
                list.splice(i, 1);
                break
            }
        }
    }
    return val
}

/*  */

/**
 * Cross-platform code generation for component v-model
 */
function genComponentModel (
    el,
    value,
    modifiers
) {
    var ref = modifiers || {};
    var number = ref.number;
    var trim = ref.trim;

    var baseValueExpression = '$$v';
    var valueExpression = baseValueExpression;
    if (trim) {
        valueExpression =
            "(typeof " + baseValueExpression + " === 'string'" +
            "? " + baseValueExpression + ".trim()" +
            ": " + baseValueExpression + ")";
    }
    if (number) {
        valueExpression = "_n(" + valueExpression + ")";
    }
    var assignment = genAssignmentCode(value, valueExpression);

    el.model = {
        value: ("(" + value + ")"),
        expression: ("\"" + value + "\""),
        callback: ("function (" + baseValueExpression + ") {" + assignment + "}")
    };
}

/**
 * Cross-platform codegen helper for generating v-model value assignment code.
 */
function genAssignmentCode (
    value,
    assignment
) {
    var modelRs = parseModel(value);
    if (modelRs.idx === null) {
        return (value + "=" + assignment)
    } else {
        return "var $$exp = " + (modelRs.exp) + ", $$idx = " + (modelRs.idx) + ";" +
            "if (!Array.isArray($$exp)){" +
            value + "=" + assignment + "}" +
            "else{$$exp.splice($$idx, 1, " + assignment + ")}"
    }
}

/**
 * parse directive model to do the array update transform. a[idx] = val => $$a.splice($$idx, 1, val)
 *
 * for loop possible cases:
 *
 * - test
 * - test[idx]
 * - test[test1[idx]]
 * - test["a"][idx]
 * - xxx.test[a[a].test1[idx]]
 * - test.xxx.a["asa"][test1[idx]]
 *
 */

var len;
var str;
var chr;
var index$1;
var expressionPos;
var expressionEndPos;

function parseModel (val) {
    str = val;
    len = str.length;
    index$1 = expressionPos = expressionEndPos = 0;

    if (val.indexOf('[') < 0 || val.lastIndexOf(']') < len - 1) {
        return {
            exp: val,
            idx: null
        }
    }

    while (!eof()) {
        chr = next();
        /* istanbul ignore if */
        if (isStringStart(chr)) {
            parseString(chr);
        } else if (chr === 0x5B) {
            parseBracket(chr);
        }
    }

    return {
        exp: val.substring(0, expressionPos),
        idx: val.substring(expressionPos + 1, expressionEndPos)
    }
}

function next () {
    return str.charCodeAt(++index$1)
}

function eof () {
    return index$1 >= len
}

function isStringStart (chr) {
    return chr === 0x22 || chr === 0x27
}

function parseBracket (chr) {
    var inBracket = 1;
    expressionPos = index$1;
    while (!eof()) {
        chr = next();
        if (isStringStart(chr)) {
            parseString(chr);
            continue
        }
        if (chr === 0x5B) { inBracket++; }
        if (chr === 0x5D) { inBracket--; }
        if (inBracket === 0) {
            expressionEndPos = index$1;
            break
        }
    }
}

function parseString (chr) {
    var stringQuote = chr;
    while (!eof()) {
        chr = next();
        if (chr === stringQuote) {
            break
        }
    }
}

/*  */

var warn$1;

// in some cases, the event used has to be determined at runtime
// so we used some reserved tokens during compile.
var RANGE_TOKEN = '__r';
var CHECKBOX_RADIO_TOKEN = '__c';

function model (
    el,
    dir,
    _warn
) {
    warn$1 = _warn;
    var value = dir.value;
    var modifiers = dir.modifiers;
    var tag = el.tag;
    var type = el.attrsMap.type;

    if (tag === 'select') {
        genSelect(el, value, modifiers);
    } else if (tag === 'input' && type === 'checkbox') {
        genCheckboxModel(el, value, modifiers);
    } else if (tag === 'input' && type === 'radio') {
        genRadioModel(el, value, modifiers);
    } else if (tag === 'input' || tag === 'textarea') {
        genDefaultModel(el, value, modifiers);
    } else if (!config$1.isReservedTag(tag)) {
        genComponentModel(el, value, modifiers);
        // component v-model doesn't need extra runtime
        return false
    } else {}

    // ensure runtime directive metadata
    return true
}

function genCheckboxModel (
    el,
    value,
    modifiers
) {
    var number = modifiers && modifiers.number;
    var valueBinding = getBindingAttr(el, 'value') || 'null';
    var trueValueBinding = getBindingAttr(el, 'true-value') || 'true';
    var falseValueBinding = getBindingAttr(el, 'false-value') || 'false';
    addProp(el, 'checked',
        "Array.isArray(" + value + ")" +
        "?_i(" + value + "," + valueBinding + ")>-1" + (
            trueValueBinding === 'true'
                ? (":(" + value + ")")
                : (":_q(" + value + "," + trueValueBinding + ")")
        )
    );
    addHandler(el, CHECKBOX_RADIO_TOKEN,
        "var $$a=" + value + "," +
        '$$el=$event.target,' +
        "$$c=$$el.checked?(" + trueValueBinding + "):(" + falseValueBinding + ");" +
        'if(Array.isArray($$a)){' +
        "var $$v=" + (number ? '_n(' + valueBinding + ')' : valueBinding) + "," +
        '$$i=_i($$a,$$v);' +
        "if($$c){$$i<0&&(" + value + "=$$a.concat($$v))}" +
        "else{$$i>-1&&(" + value + "=$$a.slice(0,$$i).concat($$a.slice($$i+1)))}" +
        "}else{" + (genAssignmentCode(value, '$$c')) + "}",
        null, true
    );
}

function genRadioModel (
    el,
    value,
    modifiers
) {
    var number = modifiers && modifiers.number;
    var valueBinding = getBindingAttr(el, 'value') || 'null';
    valueBinding = number ? ("_n(" + valueBinding + ")") : valueBinding;
    addProp(el, 'checked', ("_q(" + value + "," + valueBinding + ")"));
    addHandler(el, CHECKBOX_RADIO_TOKEN, genAssignmentCode(value, valueBinding), null, true);
}

function genSelect (
    el,
    value,
    modifiers
) {
    var number = modifiers && modifiers.number;
    var selectedVal = "Array.prototype.filter" +
        ".call($event.target.options,function(o){return o.selected})" +
        ".map(function(o){var val = \"_value\" in o ? o._value : o.value;" +
        "return " + (number ? '_n(val)' : 'val') + "})";

    var assignment = '$event.target.multiple ? $$selectedVal : $$selectedVal[0]';
    var code = "var $$selectedVal = " + selectedVal + ";";
    code = code + " " + (genAssignmentCode(value, assignment));
    addHandler(el, 'change', code, null, true);
}

function genDefaultModel (
    el,
    value,
    modifiers
) {
    var type = el.attrsMap.type;
    var ref = modifiers || {};
    var lazy = ref.lazy;
    var number = ref.number;
    var trim = ref.trim;
    var needCompositionGuard = !lazy && type !== 'range';
    var event = lazy
        ? 'change'
        : type === 'range'
            ? RANGE_TOKEN
            : 'input';

    var valueExpression = '$event.target.value';
    if (trim) {
        valueExpression = "$event.target.value.trim()";
    }
    if (number) {
        valueExpression = "_n(" + valueExpression + ")";
    }

    var code = genAssignmentCode(value, valueExpression);
    if (needCompositionGuard) {
        code = "if($event.target.composing)return;" + code;
    }

    addProp(el, 'value', ("(" + value + ")"));
    addHandler(el, event, code, null, true);
    if (trim || number || type === 'number') {
        addHandler(el, 'blur', '$forceUpdate()');
    }
}

/*  */

// normalize v-model event tokens that can only be determined at runtime.
// it's important to place the event as the first in the array because
// the whole point is ensuring the v-model callback gets called before
// user-attached handlers.
function normalizeEvents (on) {
    var event;
    /* istanbul ignore if */
    if (isDef(on[RANGE_TOKEN])) {
        // IE input[type=range] only supports `change` event
        event = isIE ? 'change' : 'input';
        on[event] = [].concat(on[RANGE_TOKEN], on[event] || []);
        delete on[RANGE_TOKEN];
    }
    if (isDef(on[CHECKBOX_RADIO_TOKEN])) {
        // Chrome fires microtasks in between click/change, leads to #4521
        event = isChrome ? 'click' : 'change';
        on[event] = [].concat(on[CHECKBOX_RADIO_TOKEN], on[event] || []);
        delete on[CHECKBOX_RADIO_TOKEN];
    }
}

var target$1;

function add$1 (
    event,
    handler,
    once$$1,
    capture,
    passive
) {
    if (once$$1) {
        var oldHandler = handler;
        var _target = target$1; // save current target element in closure
        handler = function (ev) {
            var res = arguments.length === 1
                ? oldHandler(ev)
                : oldHandler.apply(null, arguments);
            if (res !== null) {
                remove$2(event, handler, capture, _target);
            }
        };
    }
    target$1.addEventListener(
        event,
        handler,
        supportsPassive
            ? { capture: capture, passive: passive }
            : capture
    );
}

function remove$2 (
    event,
    handler,
    capture,
    _target
) {
    (_target || target$1).removeEventListener(event, handler, capture);
}

function updateDOMListeners (oldVnode, vnode) {
    if (isUndef(oldVnode.data.on) && isUndef(vnode.data.on)) {
        return
    }
    var on = vnode.data.on || {};
    var oldOn = oldVnode.data.on || {};
    target$1 = vnode.elm;
    normalizeEvents(on);
    updateListeners(on, oldOn, add$1, remove$2, vnode.context);
}

var events = {
    create: updateDOMListeners,
    update: updateDOMListeners
};

/*  */

function updateDOMProps (oldVnode, vnode) {
    if (isUndef(oldVnode.data.domProps) && isUndef(vnode.data.domProps)) {
        return
    }
    var key, cur;
    var elm = vnode.elm;
    var oldProps = oldVnode.data.domProps || {};
    var props = vnode.data.domProps || {};
    // clone observed objects, as the user probably wants to mutate it
    if (isDef(props.__ob__)) {
        props = vnode.data.domProps = extend({}, props);
    }

    for (key in oldProps) {
        if (isUndef(props[key])) {
            elm[key] = '';
        }
    }
    for (key in props) {
        cur = props[key];
        // ignore children if the node has textContent or innerHTML,
        // as these will throw away existing DOM nodes and cause removal errors
        // on subsequent patches (#3360)
        if (key === 'textContent' || key === 'innerHTML') {
            if (vnode.children) { vnode.children.length = 0; }
            if (cur === oldProps[key]) { continue }
        }

        if (key === 'value') {
            // store value as _value as well since
            // non-string values will be stringified
            elm._value = cur;
            // avoid resetting cursor position when value is the same
            var strCur = cur == null ? '' : String(cur);
            if (shouldUpdateValue(elm, vnode, strCur)) {
                elm.value = strCur;
            }
        } else {
            elm[key] = cur;
        }
    }
}

// check platforms/web/util/attrs.js acceptValue


function shouldUpdateValue (
    elm,
    vnode,
    checkVal
) {
    return (!elm.composing && (
        vnode.tag === 'option' ||
        isDirty(elm, checkVal) ||
        isInputChanged(elm, checkVal)
    ))
}

function isDirty (elm, checkVal) {
    // return true when textbox (.number and .trim) loses focus and its value is not equal to the updated value
    return document.activeElement !== elm && elm.value !== checkVal
}

function isInputChanged (elm, newVal) {
    var value = elm.value;
    var modifiers = elm._vModifiers; // injected by v-model runtime
    if ((isDef(modifiers) && modifiers.number) || elm.type === 'number') {
        return toNumber(value) !== toNumber(newVal)
    }
    if (isDef(modifiers) && modifiers.trim) {
        return value.trim() !== newVal.trim()
    }
    return value !== newVal
}

var domProps = {
    create: updateDOMProps,
    update: updateDOMProps
};

/*  */

var parseStyleText = cached(function (cssText) {
    var res = {};
    var listDelimiter = /;(?![^(]*\))/g;
    var propertyDelimiter = /:(.+)/;
    cssText.split(listDelimiter).forEach(function (item) {
        if (item) {
            var tmp = item.split(propertyDelimiter);
            tmp.length > 1 && (res[tmp[0].trim()] = tmp[1].trim());
        }
    });
    return res
});

// merge static and dynamic style data on the same vnode
function normalizeStyleData (data) {
    var style = normalizeStyleBinding(data.style);
    // static style is pre-processed into an object during compilation
    // and is always a fresh object, so it's safe to merge into it
    return data.staticStyle
        ? extend(data.staticStyle, style)
        : style
}

// normalize possible array / string values into Object
function normalizeStyleBinding (bindingStyle) {
    if (Array.isArray(bindingStyle)) {
        return toObject(bindingStyle)
    }
    if (typeof bindingStyle === 'string') {
        return parseStyleText(bindingStyle)
    }
    return bindingStyle
}

/**
 * parent component style should be after child's
 * so that parent component's style could override it
 */
function getStyle (vnode, checkChild) {
    var res = {};
    var styleData;

    if (checkChild) {
        var childNode = vnode;
        while (childNode.componentInstance) {
            childNode = childNode.componentInstance._vnode;
            if (childNode.data && (styleData = normalizeStyleData(childNode.data))) {
                extend(res, styleData);
            }
        }
    }

    if ((styleData = normalizeStyleData(vnode.data))) {
        extend(res, styleData);
    }

    var parentNode = vnode;
    while ((parentNode = parentNode.parent)) {
        if (parentNode.data && (styleData = normalizeStyleData(parentNode.data))) {
            extend(res, styleData);
        }
    }
    return res
}

/*  */

var cssVarRE = /^--/;
var importantRE = /\s*!important$/;
var setProp = function (el, name, val) {
    /* istanbul ignore if */
    if (cssVarRE.test(name)) {
        el.style.setProperty(name, val);
    } else if (importantRE.test(val)) {
        el.style.setProperty(name, val.replace(importantRE, ''), 'important');
    } else {
        var normalizedName = normalize(name);
        if (Array.isArray(val)) {
            // Support values array created by autoprefixer, e.g.
            // {display: ["-webkit-box", "-ms-flexbox", "flex"]}
            // Set them one by one, and the browser will only set those it can recognize
            for (var i = 0, len = val.length; i < len; i++) {
                el.style[normalizedName] = val[i];
            }
        } else {
            el.style[normalizedName] = val;
        }
    }
};

var prefixes = ['Webkit', 'Moz', 'ms'];

var testEl;
var normalize = cached(function (prop) {
    testEl = testEl || document.createElement('div');
    prop = camelize(prop);
    if (prop !== 'filter' && (prop in testEl.style)) {
        return prop
    }
    var upper = prop.charAt(0).toUpperCase() + prop.slice(1);
    for (var i = 0; i < prefixes.length; i++) {
        var prefixed = prefixes[i] + upper;
        if (prefixed in testEl.style) {
            return prefixed
        }
    }
});

function updateStyle (oldVnode, vnode) {
    var data = vnode.data;
    var oldData = oldVnode.data;

    if (isUndef(data.staticStyle) && isUndef(data.style) &&
        isUndef(oldData.staticStyle) && isUndef(oldData.style)) {
        return
    }

    var cur, name;
    var el = vnode.elm;
    var oldStaticStyle = oldData.staticStyle;
    var oldStyleBinding = oldData.normalizedStyle || oldData.style || {};

    // if static style exists, stylebinding already merged into it when doing normalizeStyleData
    var oldStyle = oldStaticStyle || oldStyleBinding;

    var style = normalizeStyleBinding(vnode.data.style) || {};

    // store normalized style under a different key for next diff
    // make sure to clone it if it's reactive, since the user likley wants
    // to mutate it.
    vnode.data.normalizedStyle = isDef(style.__ob__)
        ? extend({}, style)
        : style;

    var newStyle = getStyle(vnode, true);

    for (name in oldStyle) {
        if (isUndef(newStyle[name])) {
            setProp(el, name, '');
        }
    }
    for (name in newStyle) {
        cur = newStyle[name];
        if (cur !== oldStyle[name]) {
            // ie9 setting to null has no effect, must use empty string
            setProp(el, name, cur == null ? '' : cur);
        }
    }
}

var style = {
    create: updateStyle,
    update: updateStyle
};

/*  */

/**
 * Add class with compatibility for SVG since classList is not supported on
 * SVG elements in IE
 */
function addClass (el, cls) {
    /* istanbul ignore if */
    if (!cls || !(cls = cls.trim())) {
        return
    }

    /* istanbul ignore else */
    if (el.classList) {
        if (cls.indexOf(' ') > -1) {
            cls.split(/\s+/).forEach(function (c) { return el.classList.add(c); });
        } else {
            el.classList.add(cls);
        }
    } else {
        var cur = " " + (el.getAttribute('class') || '') + " ";
        if (cur.indexOf(' ' + cls + ' ') < 0) {
            el.setAttribute('class', (cur + cls).trim());
        }
    }
}

/**
 * Remove class with compatibility for SVG since classList is not supported on
 * SVG elements in IE
 */
function removeClass (el, cls) {
    /* istanbul ignore if */
    if (!cls || !(cls = cls.trim())) {
        return
    }

    /* istanbul ignore else */
    if (el.classList) {
        if (cls.indexOf(' ') > -1) {
            cls.split(/\s+/).forEach(function (c) { return el.classList.remove(c); });
        } else {
            el.classList.remove(cls);
        }
    } else {
        var cur = " " + (el.getAttribute('class') || '') + " ";
        var tar = ' ' + cls + ' ';
        while (cur.indexOf(tar) >= 0) {
            cur = cur.replace(tar, ' ');
        }
        el.setAttribute('class', cur.trim());
    }
}

/*  */

function resolveTransition (def$$1) {
    if (!def$$1) {
        return
    }
    /* istanbul ignore else */
    if (typeof def$$1 === 'object') {
        var res = {};
        if (def$$1.css !== false) {
            extend(res, autoCssTransition(def$$1.name || 'v'));
        }
        extend(res, def$$1);
        return res
    } else if (typeof def$$1 === 'string') {
        return autoCssTransition(def$$1)
    }
}

var autoCssTransition = cached(function (name) {
    return {
        enterClass: (name + "-enter"),
        enterToClass: (name + "-enter-to"),
        enterActiveClass: (name + "-enter-active"),
        leaveClass: (name + "-leave"),
        leaveToClass: (name + "-leave-to"),
        leaveActiveClass: (name + "-leave-active")
    }
});

var hasTransition = inBrowser && !isIE9;
var TRANSITION = 'transition';
var ANIMATION = 'animation';

// Transition property/event sniffing
var transitionProp = 'transition';
var transitionEndEvent = 'transitionend';
var animationProp = 'animation';
var animationEndEvent = 'animationend';
if (hasTransition) {
    /* istanbul ignore if */
    if (window.ontransitionend === undefined &&
        window.onwebkittransitionend !== undefined) {
        transitionProp = 'WebkitTransition';
        transitionEndEvent = 'webkitTransitionEnd';
    }
    if (window.onanimationend === undefined &&
        window.onwebkitanimationend !== undefined) {
        animationProp = 'WebkitAnimation';
        animationEndEvent = 'webkitAnimationEnd';
    }
}

// binding to window is necessary to make hot reload work in IE in strict mode
var raf = inBrowser && window.requestAnimationFrame
    ? window.requestAnimationFrame.bind(window)
    : setTimeout;

function nextFrame (fn) {
    raf(function () {
        raf(fn);
    });
}

function addTransitionClass (el, cls) {
    (el._transitionClasses || (el._transitionClasses = [])).push(cls);
    addClass(el, cls);
}

function removeTransitionClass (el, cls) {
    if (el._transitionClasses) {
        remove(el._transitionClasses, cls);
    }
    removeClass(el, cls);
}

function whenTransitionEnds (
    el,
    expectedType,
    cb
) {
    var ref = getTransitionInfo(el, expectedType);
    var type = ref.type;
    var timeout = ref.timeout;
    var propCount = ref.propCount;
    if (!type) { return cb() }
    var event = type === TRANSITION ? transitionEndEvent : animationEndEvent;
    var ended = 0;
    var end = function () {
        el.removeEventListener(event, onEnd);
        cb();
    };
    var onEnd = function (e) {
        if (e.target === el) {
            if (++ended >= propCount) {
                end();
            }
        }
    };
    setTimeout(function () {
        if (ended < propCount) {
            end();
        }
    }, timeout + 1);
    el.addEventListener(event, onEnd);
}

var transformRE = /\b(transform|all)(,|$)/;

function getTransitionInfo (el, expectedType) {
    var styles = window.getComputedStyle(el);
    var transitionDelays = styles[transitionProp + 'Delay'].split(', ');
    var transitionDurations = styles[transitionProp + 'Duration'].split(', ');
    var transitionTimeout = getTimeout(transitionDelays, transitionDurations);
    var animationDelays = styles[animationProp + 'Delay'].split(', ');
    var animationDurations = styles[animationProp + 'Duration'].split(', ');
    var animationTimeout = getTimeout(animationDelays, animationDurations);

    var type;
    var timeout = 0;
    var propCount = 0;
    /* istanbul ignore if */
    if (expectedType === TRANSITION) {
        if (transitionTimeout > 0) {
            type = TRANSITION;
            timeout = transitionTimeout;
            propCount = transitionDurations.length;
        }
    } else if (expectedType === ANIMATION) {
        if (animationTimeout > 0) {
            type = ANIMATION;
            timeout = animationTimeout;
            propCount = animationDurations.length;
        }
    } else {
        timeout = Math.max(transitionTimeout, animationTimeout);
        type = timeout > 0
            ? transitionTimeout > animationTimeout
                ? TRANSITION
                : ANIMATION
            : null;
        propCount = type
            ? type === TRANSITION
                ? transitionDurations.length
                : animationDurations.length
            : 0;
    }
    var hasTransform =
        type === TRANSITION &&
        transformRE.test(styles[transitionProp + 'Property']);
    return {
        type: type,
        timeout: timeout,
        propCount: propCount,
        hasTransform: hasTransform
    }
}

function getTimeout (delays, durations) {
    /* istanbul ignore next */
    while (delays.length < durations.length) {
        delays = delays.concat(delays);
    }

    return Math.max.apply(null, durations.map(function (d, i) {
        return toMs(d) + toMs(delays[i])
    }))
}

function toMs (s) {
    return Number(s.slice(0, -1)) * 1000
}

/*  */

function enter (vnode, toggleDisplay) {
    var el = vnode.elm;

    // call leave callback now
    if (isDef(el._leaveCb)) {
        el._leaveCb.cancelled = true;
        el._leaveCb();
    }

    var data = resolveTransition(vnode.data.transition);
    if (isUndef(data)) {
        return
    }

    /* istanbul ignore if */
    if (isDef(el._enterCb) || el.nodeType !== 1) {
        return
    }

    var ref = (data);
    var css = ref.css;
    var type = ref.type;
    var enterClass = ref.enterClass;
    var enterToClass = ref.enterToClass;
    var enterActiveClass = ref.enterActiveClass;
    var appearClass = ref.appearClass;
    var appearToClass = ref.appearToClass;
    var appearActiveClass = ref.appearActiveClass;
    var beforeEnter = ref.beforeEnter;
    var enter = ref.enter;
    var afterEnter = ref.afterEnter;
    var enterCancelled = ref.enterCancelled;
    var beforeAppear = ref.beforeAppear;
    var appear = ref.appear;
    var afterAppear = ref.afterAppear;
    var appearCancelled = ref.appearCancelled;
    var duration = ref.duration;

    // activeInstance will always be the <transition> component managing this
    // transition. One edge case to check is when the <transition> is placed
    // as the root node of a child component. In that case we need to check
    // <transition>'s parent for appear check.
    var context = activeInstance;
    var transitionNode = activeInstance.$vnode;
    while (transitionNode && transitionNode.parent) {
        transitionNode = transitionNode.parent;
        context = transitionNode.context;
    }

    var isAppear = !context._isMounted || !vnode.isRootInsert;

    if (isAppear && !appear && appear !== '') {
        return
    }

    var startClass = isAppear && appearClass
        ? appearClass
        : enterClass;
    var activeClass = isAppear && appearActiveClass
        ? appearActiveClass
        : enterActiveClass;
    var toClass = isAppear && appearToClass
        ? appearToClass
        : enterToClass;

    var beforeEnterHook = isAppear
        ? (beforeAppear || beforeEnter)
        : beforeEnter;
    var enterHook = isAppear
        ? (typeof appear === 'function' ? appear : enter)
        : enter;
    var afterEnterHook = isAppear
        ? (afterAppear || afterEnter)
        : afterEnter;
    var enterCancelledHook = isAppear
        ? (appearCancelled || enterCancelled)
        : enterCancelled;

    var explicitEnterDuration = toNumber(
        isObject(duration)
            ? duration.enter
            : duration
    );

    if ("production" !== 'production' && explicitEnterDuration != null) {
        checkDuration(explicitEnterDuration, 'enter', vnode);
    }

    var expectsCSS = css !== false && !isIE9;
    var userWantsControl = getHookArgumentsLength(enterHook);

    var cb = el._enterCb = once$1(function () {
        if (expectsCSS) {
            removeTransitionClass(el, toClass);
            removeTransitionClass(el, activeClass);
        }
        if (cb.cancelled) {
            if (expectsCSS) {
                removeTransitionClass(el, startClass);
            }
            enterCancelledHook && enterCancelledHook(el);
        } else {
            afterEnterHook && afterEnterHook(el);
        }
        el._enterCb = null;
    });

    if (!vnode.data.show) {
        // remove pending leave element on enter by injecting an insert hook
        mergeVNodeHook(vnode.data.hook || (vnode.data.hook = {}), 'insert', function () {
            var parent = el.parentNode;
            var pendingNode = parent && parent._pending && parent._pending[vnode.key];
            if (pendingNode &&
                pendingNode.tag === vnode.tag &&
                pendingNode.elm._leaveCb) {
                pendingNode.elm._leaveCb();
            }
            enterHook && enterHook(el, cb);
        });
    }

    // start enter transition
    beforeEnterHook && beforeEnterHook(el);
    if (expectsCSS) {
        addTransitionClass(el, startClass);
        addTransitionClass(el, activeClass);
        nextFrame(function () {
            addTransitionClass(el, toClass);
            removeTransitionClass(el, startClass);
            if (!cb.cancelled && !userWantsControl) {
                if (isValidDuration(explicitEnterDuration)) {
                    setTimeout(cb, explicitEnterDuration);
                } else {
                    whenTransitionEnds(el, type, cb);
                }
            }
        });
    }

    if (vnode.data.show) {
        toggleDisplay && toggleDisplay();
        enterHook && enterHook(el, cb);
    }

    if (!expectsCSS && !userWantsControl) {
        cb();
    }
}

function leave (vnode, rm) {
    var el = vnode.elm;

    // call enter callback now
    if (isDef(el._enterCb)) {
        el._enterCb.cancelled = true;
        el._enterCb();
    }

    var data = resolveTransition(vnode.data.transition);
    if (isUndef(data)) {
        return rm()
    }

    /* istanbul ignore if */
    if (isDef(el._leaveCb) || el.nodeType !== 1) {
        return
    }

    var ref = (data);
    var css = ref.css;
    var type = ref.type;
    var leaveClass = ref.leaveClass;
    var leaveToClass = ref.leaveToClass;
    var leaveActiveClass = ref.leaveActiveClass;
    var beforeLeave = ref.beforeLeave;
    var leave = ref.leave;
    var afterLeave = ref.afterLeave;
    var leaveCancelled = ref.leaveCancelled;
    var delayLeave = ref.delayLeave;
    var duration = ref.duration;

    var expectsCSS = css !== false && !isIE9;
    var userWantsControl = getHookArgumentsLength(leave);

    var explicitLeaveDuration = toNumber(
        isObject(duration)
            ? duration.leave
            : duration
    );

    if ("production" !== 'production' && explicitLeaveDuration != null) {
        checkDuration(explicitLeaveDuration, 'leave', vnode);
    }

    var cb = el._leaveCb = once$1(function () {
        if (el.parentNode && el.parentNode._pending) {
            el.parentNode._pending[vnode.key] = null;
        }
        if (expectsCSS) {
            removeTransitionClass(el, leaveToClass);
            removeTransitionClass(el, leaveActiveClass);
        }
        if (cb.cancelled) {
            if (expectsCSS) {
                removeTransitionClass(el, leaveClass);
            }
            leaveCancelled && leaveCancelled(el);
        } else {
            rm();
            afterLeave && afterLeave(el);
        }
        el._leaveCb = null;
    });

    if (delayLeave) {
        delayLeave(performLeave);
    } else {
        performLeave();
    }

    function performLeave () {
        // the delayed leave may have already been cancelled
        if (cb.cancelled) {
            return
        }
        // record leaving element
        if (!vnode.data.show) {
            (el.parentNode._pending || (el.parentNode._pending = {}))[vnode.key] = vnode;
        }
        beforeLeave && beforeLeave(el);
        if (expectsCSS) {
            addTransitionClass(el, leaveClass);
            addTransitionClass(el, leaveActiveClass);
            nextFrame(function () {
                addTransitionClass(el, leaveToClass);
                removeTransitionClass(el, leaveClass);
                if (!cb.cancelled && !userWantsControl) {
                    if (isValidDuration(explicitLeaveDuration)) {
                        setTimeout(cb, explicitLeaveDuration);
                    } else {
                        whenTransitionEnds(el, type, cb);
                    }
                }
            });
        }
        leave && leave(el, cb);
        if (!expectsCSS && !userWantsControl) {
            cb();
        }
    }
}

// only used in dev mode
function checkDuration (val, name, vnode) {
    if (typeof val !== 'number') {
        warn(
            "<transition> explicit " + name + " duration is not a valid number - " +
            "got " + (JSON.stringify(val)) + ".",
            vnode.context
        );
    } else if (isNaN(val)) {
        warn(
            "<transition> explicit " + name + " duration is NaN - " +
            'the duration expression might be incorrect.',
            vnode.context
        );
    }
}

function isValidDuration (val) {
    return typeof val === 'number' && !isNaN(val)
}

/**
 * Normalize a transition hook's argument length. The hook may be:
 * - a merged hook (invoker) with the original in .fns
 * - a wrapped component method (check ._length)
 * - a plain function (.length)
 */
function getHookArgumentsLength (fn) {
    if (isUndef(fn)) {
        return false
    }
    var invokerFns = fn.fns;
    if (isDef(invokerFns)) {
        // invoker
        return getHookArgumentsLength(
            Array.isArray(invokerFns)
                ? invokerFns[0]
                : invokerFns
        )
    } else {
        return (fn._length || fn.length) > 1
    }
}

function _enter (_, vnode) {
    if (vnode.data.show !== true) {
        enter(vnode);
    }
}

var transition = inBrowser ? {
    create: _enter,
    activate: _enter,
    remove: function remove$$1 (vnode, rm) {
        /* istanbul ignore else */
        if (vnode.data.show !== true) {
            leave(vnode, rm);
        } else {
            rm();
        }
    }
} : {};

var platformModules = [
    attrs,
    klass,
    events,
    domProps,
    style,
    transition
];

/*  */

// the directive module should be applied last, after all
// built-in modules have been applied.
var modules = platformModules.concat(baseModules);

var patch = createPatchFunction({ nodeOps: nodeOps, modules: modules });

/**
 * Not type checking this file because flow doesn't like attaching
 * properties to Elements.
 */

/* istanbul ignore if */
if (isIE9) {
    // http://www.matts411.com/post/internet-explorer-9-oninput/
    document.addEventListener('selectionchange', function () {
        var el = document.activeElement;
        if (el && el.vmodel) {
            trigger(el, 'input');
        }
    });
}

var model$1 = {
    inserted: function inserted (el, binding, vnode) {
        if (vnode.tag === 'select') {
            var cb = function () {
                setSelected(el, binding, vnode.context);
            };
            cb();
            /* istanbul ignore if */
            if (isIE || isEdge) {
                setTimeout(cb, 0);
            }
        } else if (vnode.tag === 'textarea' || el.type === 'text' || el.type === 'password') {
            el._vModifiers = binding.modifiers;
            if (!binding.modifiers.lazy) {
                // Safari < 10.2 & UIWebView doesn't fire compositionend when
                // switching focus before confirming composition choice
                // this also fixes the issue where some browsers e.g. iOS Chrome
                // fires "change" instead of "input" on autocomplete.
                el.addEventListener('change', onCompositionEnd);
                if (!isAndroid) {
                    el.addEventListener('compositionstart', onCompositionStart);
                    el.addEventListener('compositionend', onCompositionEnd);
                }
                /* istanbul ignore if */
                if (isIE9) {
                    el.vmodel = true;
                }
            }
        }
    },
    componentUpdated: function componentUpdated (el, binding, vnode) {
        if (vnode.tag === 'select') {
            setSelected(el, binding, vnode.context);
            // in case the options rendered by v-for have changed,
            // it's possible that the value is out-of-sync with the rendered options.
            // detect such cases and filter out values that no longer has a matching
            // option in the DOM.
            var needReset = el.multiple
                ? binding.value.some(function (v) { return hasNoMatchingOption(v, el.options); })
                : binding.value !== binding.oldValue && hasNoMatchingOption(binding.value, el.options);
            if (needReset) {
                trigger(el, 'change');
            }
        }
    }
};

function setSelected (el, binding, vm) {
    var value = binding.value;
    var isMultiple = el.multiple;
    if (isMultiple && !Array.isArray(value)) {
        "production" !== 'production' && warn(
            "<select multiple v-model=\"" + (binding.expression) + "\"> " +
            "expects an Array value for its binding, but got " + (Object.prototype.toString.call(value).slice(8, -1)),
            vm
        );
        return
    }
    var selected, option;
    for (var i = 0, l = el.options.length; i < l; i++) {
        option = el.options[i];
        if (isMultiple) {
            selected = looseIndexOf(value, getValue(option)) > -1;
            if (option.selected !== selected) {
                option.selected = selected;
            }
        } else {
            if (looseEqual(getValue(option), value)) {
                if (el.selectedIndex !== i) {
                    el.selectedIndex = i;
                }
                return
            }
        }
    }
    if (!isMultiple) {
        el.selectedIndex = -1;
    }
}

function hasNoMatchingOption (value, options) {
    for (var i = 0, l = options.length; i < l; i++) {
        if (looseEqual(getValue(options[i]), value)) {
            return false
        }
    }
    return true
}

function getValue (option) {
    return '_value' in option
        ? option._value
        : option.value
}

function onCompositionStart (e) {
    e.target.composing = true;
}

function onCompositionEnd (e) {
    e.target.composing = false;
    trigger(e.target, 'input');
}

function trigger (el, type) {
    var e = document.createEvent('HTMLEvents');
    e.initEvent(type, true, true);
    el.dispatchEvent(e);
}

/*  */

// recursively search for possible transition defined inside the component root
function locateNode (vnode) {
    return vnode.componentInstance && (!vnode.data || !vnode.data.transition)
        ? locateNode(vnode.componentInstance._vnode)
        : vnode
}

var show = {
    bind: function bind (el, ref, vnode) {
        var value = ref.value;

        vnode = locateNode(vnode);
        var transition = vnode.data && vnode.data.transition;
        var originalDisplay = el.__vOriginalDisplay =
            el.style.display === 'none' ? '' : el.style.display;
        if (value && transition && !isIE9) {
            vnode.data.show = true;
            enter(vnode, function () {
                el.style.display = originalDisplay;
            });
        } else {
            el.style.display = value ? originalDisplay : 'none';
        }
    },

    update: function update (el, ref, vnode) {
        var value = ref.value;
        var oldValue = ref.oldValue;

        /* istanbul ignore if */
        if (value === oldValue) { return }
        vnode = locateNode(vnode);
        var transition = vnode.data && vnode.data.transition;
        if (transition && !isIE9) {
            vnode.data.show = true;
            if (value) {
                enter(vnode, function () {
                    el.style.display = el.__vOriginalDisplay;
                });
            } else {
                leave(vnode, function () {
                    el.style.display = 'none';
                });
            }
        } else {
            el.style.display = value ? el.__vOriginalDisplay : 'none';
        }
    },

    unbind: function unbind (
        el,
        binding,
        vnode,
        oldVnode,
        isDestroy
    ) {
        if (!isDestroy) {
            el.style.display = el.__vOriginalDisplay;
        }
    }
};

var platformDirectives = {
    model: model$1,
    show: show
};

/*  */

// Provides transition support for a single element/component.
// supports transition mode (out-in / in-out)

var transitionProps = {
    name: String,
    appear: Boolean,
    css: Boolean,
    mode: String,
    type: String,
    enterClass: String,
    leaveClass: String,
    enterToClass: String,
    leaveToClass: String,
    enterActiveClass: String,
    leaveActiveClass: String,
    appearClass: String,
    appearActiveClass: String,
    appearToClass: String,
    duration: [Number, String, Object]
};

// in case the child is also an abstract component, e.g. <keep-alive>
// we want to recursively retrieve the real component to be rendered
function getRealChild (vnode) {
    var compOptions = vnode && vnode.componentOptions;
    if (compOptions && compOptions.Ctor.options.abstract) {
        return getRealChild(getFirstComponentChild(compOptions.children))
    } else {
        return vnode
    }
}

function extractTransitionData (comp) {
    var data = {};
    var options = comp.$options;
    // props
    for (var key in options.propsData) {
        data[key] = comp[key];
    }
    // events.
    // extract listeners and pass them directly to the transition methods
    var listeners = options._parentListeners;
    for (var key$1 in listeners) {
        data[camelize(key$1)] = listeners[key$1];
    }
    return data
}

function placeholder (h, rawChild) {
    if (/\d-keep-alive$/.test(rawChild.tag)) {
        return h('keep-alive', {
            props: rawChild.componentOptions.propsData
        })
    }
}

function hasParentTransition (vnode) {
    while ((vnode = vnode.parent)) {
        if (vnode.data.transition) {
            return true
        }
    }
}

function isSameChild (child, oldChild) {
    return oldChild.key === child.key && oldChild.tag === child.tag
}

var Transition = {
    name: 'transition',
    props: transitionProps,
    abstract: true,

    render: function render (h) {
        var this$1 = this;

        var children = this.$slots.default;
        if (!children) {
            return
        }

        // filter out text nodes (possible whitespaces)
        children = children.filter(function (c) { return c.tag; });
        /* istanbul ignore if */
        if (!children.length) {
            return
        }

        // warn multiple elements
        if ("production" !== 'production' && children.length > 1) {
            warn(
                '<transition> can only be used on a single element. Use ' +
                '<transition-group> for lists.',
                this.$parent
            );
        }

        var mode = this.mode;

        // warn invalid mode
        if ("production" !== 'production' &&
            mode && mode !== 'in-out' && mode !== 'out-in') {
            warn(
                'invalid <transition> mode: ' + mode,
                this.$parent
            );
        }

        var rawChild = children[0];

        // if this is a component root node and the component's
        // parent container node also has transition, skip.
        if (hasParentTransition(this.$vnode)) {
            return rawChild
        }

        // apply transition data to child
        // use getRealChild() to ignore abstract components e.g. keep-alive
        var child = getRealChild(rawChild);
        /* istanbul ignore if */
        if (!child) {
            return rawChild
        }

        if (this._leaving) {
            return placeholder(h, rawChild)
        }

        // ensure a key that is unique to the vnode type and to this transition
        // component instance. This key will be used to remove pending leaving nodes
        // during entering.
        var id = "__transition-" + (this._uid) + "-";
        child.key = child.key == null
            ? id + child.tag
            : isPrimitive(child.key)
                ? (String(child.key).indexOf(id) === 0 ? child.key : id + child.key)
                : child.key;

        var data = (child.data || (child.data = {})).transition = extractTransitionData(this);
        var oldRawChild = this._vnode;
        var oldChild = getRealChild(oldRawChild);

        // mark v-show
        // so that the transition module can hand over the control to the directive
        if (child.data.directives && child.data.directives.some(function (d) { return d.name === 'show'; })) {
            child.data.show = true;
        }

        if (oldChild && oldChild.data && !isSameChild(child, oldChild)) {
            // replace old child transition data with fresh one
            // important for dynamic transitions!
            var oldData = oldChild && (oldChild.data.transition = extend({}, data));
            // handle transition mode
            if (mode === 'out-in') {
                // return placeholder node and queue update when leave finishes
                this._leaving = true;
                mergeVNodeHook(oldData, 'afterLeave', function () {
                    this$1._leaving = false;
                    this$1.$forceUpdate();
                });
                return placeholder(h, rawChild)
            } else if (mode === 'in-out') {
                var delayedLeave;
                var performLeave = function () { delayedLeave(); };
                mergeVNodeHook(data, 'afterEnter', performLeave);
                mergeVNodeHook(data, 'enterCancelled', performLeave);
                mergeVNodeHook(oldData, 'delayLeave', function (leave) { delayedLeave = leave; });
            }
        }

        return rawChild
    }
};

/*  */

// Provides transition support for list items.
// supports move transitions using the FLIP technique.

// Because the vdom's children update algorithm is "unstable" - i.e.
// it doesn't guarantee the relative positioning of removed elements,
// we force transition-group to update its children into two passes:
// in the first pass, we remove all nodes that need to be removed,
// triggering their leaving transition; in the second pass, we insert/move
// into the final desired state. This way in the second pass removed
// nodes will remain where they should be.

var props = extend({
    tag: String,
    moveClass: String
}, transitionProps);

delete props.mode;

var TransitionGroup = {
    props: props,

    render: function render (h) {
        var tag = this.tag || this.$vnode.data.tag || 'span';
        var map = Object.create(null);
        var prevChildren = this.prevChildren = this.children;
        var rawChildren = this.$slots.default || [];
        var children = this.children = [];
        var transitionData = extractTransitionData(this);

        for (var i = 0; i < rawChildren.length; i++) {
            var c = rawChildren[i];
            if (c.tag) {
                if (c.key != null && String(c.key).indexOf('__vlist') !== 0) {
                    children.push(c);
                    map[c.key] = c
                    ;(c.data || (c.data = {})).transition = transitionData;
                } else {}
            }
        }

        if (prevChildren) {
            var kept = [];
            var removed = [];
            for (var i$1 = 0; i$1 < prevChildren.length; i$1++) {
                var c$1 = prevChildren[i$1];
                c$1.data.transition = transitionData;
                c$1.data.pos = c$1.elm.getBoundingClientRect();
                if (map[c$1.key]) {
                    kept.push(c$1);
                } else {
                    removed.push(c$1);
                }
            }
            this.kept = h(tag, null, kept);
            this.removed = removed;
        }

        return h(tag, null, children)
    },

    beforeUpdate: function beforeUpdate () {
        // force removing pass
        this.__patch__(
            this._vnode,
            this.kept,
            false, // hydrating
            true // removeOnly (!important, avoids unnecessary moves)
        );
        this._vnode = this.kept;
    },

    updated: function updated () {
        var children = this.prevChildren;
        var moveClass = this.moveClass || ((this.name || 'v') + '-move');
        if (!children.length || !this.hasMove(children[0].elm, moveClass)) {
            return
        }

        // we divide the work into three loops to avoid mixing DOM reads and writes
        // in each iteration - which helps prevent layout thrashing.
        children.forEach(callPendingCbs);
        children.forEach(recordPosition);
        children.forEach(applyTranslation);

        // force reflow to put everything in position
        var body = document.body;
        var f = body.offsetHeight; // eslint-disable-line

        children.forEach(function (c) {
            if (c.data.moved) {
                var el = c.elm;
                var s = el.style;
                addTransitionClass(el, moveClass);
                s.transform = s.WebkitTransform = s.transitionDuration = '';
                el.addEventListener(transitionEndEvent, el._moveCb = function cb (e) {
                    if (!e || /transform$/.test(e.propertyName)) {
                        el.removeEventListener(transitionEndEvent, cb);
                        el._moveCb = null;
                        removeTransitionClass(el, moveClass);
                    }
                });
            }
        });
    },

    methods: {
        hasMove: function hasMove (el, moveClass) {
            /* istanbul ignore if */
            if (!hasTransition) {
                return false
            }
            if (this._hasMove != null) {
                return this._hasMove
            }
            // Detect whether an element with the move class applied has
            // CSS transitions. Since the element may be inside an entering
            // transition at this very moment, we make a clone of it and remove
            // all other transition classes applied to ensure only the move class
            // is applied.
            var clone = el.cloneNode();
            if (el._transitionClasses) {
                el._transitionClasses.forEach(function (cls) { removeClass(clone, cls); });
            }
            addClass(clone, moveClass);
            clone.style.display = 'none';
            this.$el.appendChild(clone);
            var info = getTransitionInfo(clone);
            this.$el.removeChild(clone);
            return (this._hasMove = info.hasTransform)
        }
    }
};

function callPendingCbs (c) {
    /* istanbul ignore if */
    if (c.elm._moveCb) {
        c.elm._moveCb();
    }
    /* istanbul ignore if */
    if (c.elm._enterCb) {
        c.elm._enterCb();
    }
}

function recordPosition (c) {
    c.data.newPos = c.elm.getBoundingClientRect();
}

function applyTranslation (c) {
    var oldPos = c.data.pos;
    var newPos = c.data.newPos;
    var dx = oldPos.left - newPos.left;
    var dy = oldPos.top - newPos.top;
    if (dx || dy) {
        c.data.moved = true;
        var s = c.elm.style;
        s.transform = s.WebkitTransform = "translate(" + dx + "px," + dy + "px)";
        s.transitionDuration = '0s';
    }
}

var platformComponents = {
    Transition: Transition,
    TransitionGroup: TransitionGroup
};

/*  */

// install platform specific utils
Vue$3.config.mustUseProp = mustUseProp;
Vue$3.config.isReservedTag = isReservedTag;
Vue$3.config.isReservedAttr = isReservedAttr;
Vue$3.config.getTagNamespace = getTagNamespace;
Vue$3.config.isUnknownElement = isUnknownElement;

// install platform runtime directives & components
extend(Vue$3.options.directives, platformDirectives);
extend(Vue$3.options.components, platformComponents);

// install platform patch function
Vue$3.prototype.__patch__ = inBrowser ? patch : noop$1;

// public mount method
Vue$3.prototype.$mount = function (
    el,
    hydrating
) {
    el = el && inBrowser ? query(el) : undefined;
    return mountComponent(this, el, hydrating)
};

// devtools global hook
/* istanbul ignore next */
setTimeout(function () {
    if (config$1.devtools) {
        if (devtools) {
            devtools.emit('init', Vue$3);
        } else if ("production" !== 'production' && isChrome) {
            console[console.info ? 'info' : 'log'](
                'Download the Vue Devtools extension for a better development experience:\n' +
                'https://github.com/vuejs/vue-devtools'
            );
        }
    }
    if ("production" !== 'production' &&
        config$1.productionTip !== false &&
        inBrowser && typeof console !== 'undefined') {
        console[console.info ? 'info' : 'log'](
            "You are running Vue in development mode.\n" +
            "Make sure to turn on production mode when deploying for production.\n" +
            "See more tips at https://vuejs.org/guide/deployment.html"
        );
    }
}, 0);

/*  */

// check whether current browser encodes a char inside attribute values
function shouldDecode (content, encoded) {
    var div = document.createElement('div');
    div.innerHTML = "<div a=\"" + content + "\">";
    return div.innerHTML.indexOf(encoded) > 0
}

// #3663
// IE encodes newlines inside attribute values while other browsers don't
var shouldDecodeNewlines = inBrowser ? shouldDecode('\n', '&#10;') : false;

/*  */

var isUnaryTag = makeMap(
    'area,base,br,col,embed,frame,hr,img,input,isindex,keygen,' +
    'link,meta,param,source,track,wbr'
);

// Elements that you can, intentionally, leave open
// (and which close themselves)
var canBeLeftOpenTag = makeMap(
    'colgroup,dd,dt,li,options,p,td,tfoot,th,thead,tr,source'
);

// HTML5 tags https://html.spec.whatwg.org/multipage/indices.html#elements-3
// Phrasing Content https://html.spec.whatwg.org/multipage/dom.html#phrasing-content
var isNonPhrasingTag = makeMap(
    'address,article,aside,base,blockquote,body,caption,col,colgroup,dd,' +
    'details,dialog,div,dl,dt,fieldset,figcaption,figure,footer,form,' +
    'h1,h2,h3,h4,h5,h6,head,header,hgroup,hr,html,legend,li,menuitem,meta,' +
    'optgroup,option,param,rp,rt,source,style,summary,tbody,td,tfoot,th,thead,' +
    'title,tr,track'
);

var commonjsGlobal = typeof window !== 'undefined' ? window : typeof global !== 'undefined' ? global : typeof self !== 'undefined' ? self : {};





function createCommonjsModule(fn, module) {
    return module = { exports: {} }, fn(module, module.exports), module.exports;
}

var he = createCommonjsModule(function (module, exports) {
    /*! https://mths.be/he v1.1.1 by @mathias | MIT license */
    (function(root) {

        // Detect free variables `exports`.
        var freeExports = 'object' == 'object' && exports;

        // Detect free variable `module`.
        var freeModule = 'object' == 'object' && module &&
            module.exports == freeExports && module;

        // Detect free variable `global`, from Node.js or Browserified code,
        // and use it as `root`.
        var freeGlobal = typeof commonjsGlobal == 'object' && commonjsGlobal;
        if (freeGlobal.global === freeGlobal || freeGlobal.window === freeGlobal) {
            root = freeGlobal;
        }

        /*--------------------------------------------------------------------------*/

        // All astral symbols.
        var regexAstralSymbols = /[\uD800-\uDBFF][\uDC00-\uDFFF]/g;
        // All ASCII symbols (not just printable ASCII) except those listed in the
        // first column of the overrides table.
        // https://html.spec.whatwg.org/multipage/syntax.html#table-charref-overrides
        var regexAsciiWhitelist = /[\x01-\x7F]/g;
        // All BMP symbols that are not ASCII newlines, printable ASCII symbols, or
        // code points listed in the first column of the overrides table on
        // https://html.spec.whatwg.org/multipage/syntax.html#table-charref-overrides.
        var regexBmpWhitelist = /[\x01-\t\x0B\f\x0E-\x1F\x7F\x81\x8D\x8F\x90\x9D\xA0-\uFFFF]/g;

        var regexEncodeNonAscii = /<\u20D2|=\u20E5|>\u20D2|\u205F\u200A|\u219D\u0338|\u2202\u0338|\u2220\u20D2|\u2229\uFE00|\u222A\uFE00|\u223C\u20D2|\u223D\u0331|\u223E\u0333|\u2242\u0338|\u224B\u0338|\u224D\u20D2|\u224E\u0338|\u224F\u0338|\u2250\u0338|\u2261\u20E5|\u2264\u20D2|\u2265\u20D2|\u2266\u0338|\u2267\u0338|\u2268\uFE00|\u2269\uFE00|\u226A\u0338|\u226A\u20D2|\u226B\u0338|\u226B\u20D2|\u227F\u0338|\u2282\u20D2|\u2283\u20D2|\u228A\uFE00|\u228B\uFE00|\u228F\u0338|\u2290\u0338|\u2293\uFE00|\u2294\uFE00|\u22B4\u20D2|\u22B5\u20D2|\u22D8\u0338|\u22D9\u0338|\u22DA\uFE00|\u22DB\uFE00|\u22F5\u0338|\u22F9\u0338|\u2933\u0338|\u29CF\u0338|\u29D0\u0338|\u2A6D\u0338|\u2A70\u0338|\u2A7D\u0338|\u2A7E\u0338|\u2AA1\u0338|\u2AA2\u0338|\u2AAC\uFE00|\u2AAD\uFE00|\u2AAF\u0338|\u2AB0\u0338|\u2AC5\u0338|\u2AC6\u0338|\u2ACB\uFE00|\u2ACC\uFE00|\u2AFD\u20E5|[\xA0-\u0113\u0116-\u0122\u0124-\u012B\u012E-\u014D\u0150-\u017E\u0192\u01B5\u01F5\u0237\u02C6\u02C7\u02D8-\u02DD\u0311\u0391-\u03A1\u03A3-\u03A9\u03B1-\u03C9\u03D1\u03D2\u03D5\u03D6\u03DC\u03DD\u03F0\u03F1\u03F5\u03F6\u0401-\u040C\u040E-\u044F\u0451-\u045C\u045E\u045F\u2002-\u2005\u2007-\u2010\u2013-\u2016\u2018-\u201A\u201C-\u201E\u2020-\u2022\u2025\u2026\u2030-\u2035\u2039\u203A\u203E\u2041\u2043\u2044\u204F\u2057\u205F-\u2063\u20AC\u20DB\u20DC\u2102\u2105\u210A-\u2113\u2115-\u211E\u2122\u2124\u2127-\u2129\u212C\u212D\u212F-\u2131\u2133-\u2138\u2145-\u2148\u2153-\u215E\u2190-\u219B\u219D-\u21A7\u21A9-\u21AE\u21B0-\u21B3\u21B5-\u21B7\u21BA-\u21DB\u21DD\u21E4\u21E5\u21F5\u21FD-\u2205\u2207-\u2209\u220B\u220C\u220F-\u2214\u2216-\u2218\u221A\u221D-\u2238\u223A-\u2257\u2259\u225A\u225C\u225F-\u2262\u2264-\u228B\u228D-\u229B\u229D-\u22A5\u22A7-\u22B0\u22B2-\u22BB\u22BD-\u22DB\u22DE-\u22E3\u22E6-\u22F7\u22F9-\u22FE\u2305\u2306\u2308-\u2310\u2312\u2313\u2315\u2316\u231C-\u231F\u2322\u2323\u232D\u232E\u2336\u233D\u233F\u237C\u23B0\u23B1\u23B4-\u23B6\u23DC-\u23DF\u23E2\u23E7\u2423\u24C8\u2500\u2502\u250C\u2510\u2514\u2518\u251C\u2524\u252C\u2534\u253C\u2550-\u256C\u2580\u2584\u2588\u2591-\u2593\u25A1\u25AA\u25AB\u25AD\u25AE\u25B1\u25B3-\u25B5\u25B8\u25B9\u25BD-\u25BF\u25C2\u25C3\u25CA\u25CB\u25EC\u25EF\u25F8-\u25FC\u2605\u2606\u260E\u2640\u2642\u2660\u2663\u2665\u2666\u266A\u266D-\u266F\u2713\u2717\u2720\u2736\u2758\u2772\u2773\u27C8\u27C9\u27E6-\u27ED\u27F5-\u27FA\u27FC\u27FF\u2902-\u2905\u290C-\u2913\u2916\u2919-\u2920\u2923-\u292A\u2933\u2935-\u2939\u293C\u293D\u2945\u2948-\u294B\u294E-\u2976\u2978\u2979\u297B-\u297F\u2985\u2986\u298B-\u2996\u299A\u299C\u299D\u29A4-\u29B7\u29B9\u29BB\u29BC\u29BE-\u29C5\u29C9\u29CD-\u29D0\u29DC-\u29DE\u29E3-\u29E5\u29EB\u29F4\u29F6\u2A00-\u2A02\u2A04\u2A06\u2A0C\u2A0D\u2A10-\u2A17\u2A22-\u2A27\u2A29\u2A2A\u2A2D-\u2A31\u2A33-\u2A3C\u2A3F\u2A40\u2A42-\u2A4D\u2A50\u2A53-\u2A58\u2A5A-\u2A5D\u2A5F\u2A66\u2A6A\u2A6D-\u2A75\u2A77-\u2A9A\u2A9D-\u2AA2\u2AA4-\u2AB0\u2AB3-\u2AC8\u2ACB\u2ACC\u2ACF-\u2ADB\u2AE4\u2AE6-\u2AE9\u2AEB-\u2AF3\u2AFD\uFB00-\uFB04]|\uD835[\uDC9C\uDC9E\uDC9F\uDCA2\uDCA5\uDCA6\uDCA9-\uDCAC\uDCAE-\uDCB9\uDCBB\uDCBD-\uDCC3\uDCC5-\uDCCF\uDD04\uDD05\uDD07-\uDD0A\uDD0D-\uDD14\uDD16-\uDD1C\uDD1E-\uDD39\uDD3B-\uDD3E\uDD40-\uDD44\uDD46\uDD4A-\uDD50\uDD52-\uDD6B]/g;
        var encodeMap = {'\xAD':'shy','\u200C':'zwnj','\u200D':'zwj','\u200E':'lrm','\u2063':'ic','\u2062':'it','\u2061':'af','\u200F':'rlm','\u200B':'ZeroWidthSpace','\u2060':'NoBreak','\u0311':'DownBreve','\u20DB':'tdot','\u20DC':'DotDot','\t':'Tab','\n':'NewLine','\u2008':'puncsp','\u205F':'MediumSpace','\u2009':'thinsp','\u200A':'hairsp','\u2004':'emsp13','\u2002':'ensp','\u2005':'emsp14','\u2003':'emsp','\u2007':'numsp','\xA0':'nbsp','\u205F\u200A':'ThickSpace','\u203E':'oline','_':'lowbar','\u2010':'dash','\u2013':'ndash','\u2014':'mdash','\u2015':'horbar',',':'comma',';':'semi','\u204F':'bsemi',':':'colon','\u2A74':'Colone','!':'excl','\xA1':'iexcl','?':'quest','\xBF':'iquest','.':'period','\u2025':'nldr','\u2026':'mldr','\xB7':'middot','\'':'apos','\u2018':'lsquo','\u2019':'rsquo','\u201A':'sbquo','\u2039':'lsaquo','\u203A':'rsaquo','"':'quot','\u201C':'ldquo','\u201D':'rdquo','\u201E':'bdquo','\xAB':'laquo','\xBB':'raquo','(':'lpar',')':'rpar','[':'lsqb',']':'rsqb','{':'lcub','}':'rcub','\u2308':'lceil','\u2309':'rceil','\u230A':'lfloor','\u230B':'rfloor','\u2985':'lopar','\u2986':'ropar','\u298B':'lbrke','\u298C':'rbrke','\u298D':'lbrkslu','\u298E':'rbrksld','\u298F':'lbrksld','\u2990':'rbrkslu','\u2991':'langd','\u2992':'rangd','\u2993':'lparlt','\u2994':'rpargt','\u2995':'gtlPar','\u2996':'ltrPar','\u27E6':'lobrk','\u27E7':'robrk','\u27E8':'lang','\u27E9':'rang','\u27EA':'Lang','\u27EB':'Rang','\u27EC':'loang','\u27ED':'roang','\u2772':'lbbrk','\u2773':'rbbrk','\u2016':'Vert','\xA7':'sect','\xB6':'para','@':'commat','*':'ast','/':'sol','undefined':null,'&':'amp','#':'num','%':'percnt','\u2030':'permil','\u2031':'pertenk','\u2020':'dagger','\u2021':'Dagger','\u2022':'bull','\u2043':'hybull','\u2032':'prime','\u2033':'Prime','\u2034':'tprime','\u2057':'qprime','\u2035':'bprime','\u2041':'caret','`':'grave','\xB4':'acute','\u02DC':'tilde','^':'Hat','\xAF':'macr','\u02D8':'breve','\u02D9':'dot','\xA8':'die','\u02DA':'ring','\u02DD':'dblac','\xB8':'cedil','\u02DB':'ogon','\u02C6':'circ','\u02C7':'caron','\xB0':'deg','\xA9':'copy','\xAE':'reg','\u2117':'copysr','\u2118':'wp','\u211E':'rx','\u2127':'mho','\u2129':'iiota','\u2190':'larr','\u219A':'nlarr','\u2192':'rarr','\u219B':'nrarr','\u2191':'uarr','\u2193':'darr','\u2194':'harr','\u21AE':'nharr','\u2195':'varr','\u2196':'nwarr','\u2197':'nearr','\u2198':'searr','\u2199':'swarr','\u219D':'rarrw','\u219D\u0338':'nrarrw','\u219E':'Larr','\u219F':'Uarr','\u21A0':'Rarr','\u21A1':'Darr','\u21A2':'larrtl','\u21A3':'rarrtl','\u21A4':'mapstoleft','\u21A5':'mapstoup','\u21A6':'map','\u21A7':'mapstodown','\u21A9':'larrhk','\u21AA':'rarrhk','\u21AB':'larrlp','\u21AC':'rarrlp','\u21AD':'harrw','\u21B0':'lsh','\u21B1':'rsh','\u21B2':'ldsh','\u21B3':'rdsh','\u21B5':'crarr','\u21B6':'cularr','\u21B7':'curarr','\u21BA':'olarr','\u21BB':'orarr','\u21BC':'lharu','\u21BD':'lhard','\u21BE':'uharr','\u21BF':'uharl','\u21C0':'rharu','\u21C1':'rhard','\u21C2':'dharr','\u21C3':'dharl','\u21C4':'rlarr','\u21C5':'udarr','\u21C6':'lrarr','\u21C7':'llarr','\u21C8':'uuarr','\u21C9':'rrarr','\u21CA':'ddarr','\u21CB':'lrhar','\u21CC':'rlhar','\u21D0':'lArr','\u21CD':'nlArr','\u21D1':'uArr','\u21D2':'rArr','\u21CF':'nrArr','\u21D3':'dArr','\u21D4':'iff','\u21CE':'nhArr','\u21D5':'vArr','\u21D6':'nwArr','\u21D7':'neArr','\u21D8':'seArr','\u21D9':'swArr','\u21DA':'lAarr','\u21DB':'rAarr','\u21DD':'zigrarr','\u21E4':'larrb','\u21E5':'rarrb','\u21F5':'duarr','\u21FD':'loarr','\u21FE':'roarr','\u21FF':'hoarr','\u2200':'forall','\u2201':'comp','\u2202':'part','\u2202\u0338':'npart','\u2203':'exist','\u2204':'nexist','\u2205':'empty','\u2207':'Del','\u2208':'in','\u2209':'notin','\u220B':'ni','\u220C':'notni','\u03F6':'bepsi','\u220F':'prod','\u2210':'coprod','\u2211':'sum','+':'plus','\xB1':'pm','\xF7':'div','\xD7':'times','<':'lt','\u226E':'nlt','<\u20D2':'nvlt','=':'equals','\u2260':'ne','=\u20E5':'bne','\u2A75':'Equal','>':'gt','\u226F':'ngt','>\u20D2':'nvgt','\xAC':'not','|':'vert','\xA6':'brvbar','\u2212':'minus','\u2213':'mp','\u2214':'plusdo','\u2044':'frasl','\u2216':'setmn','\u2217':'lowast','\u2218':'compfn','\u221A':'Sqrt','\u221D':'prop','\u221E':'infin','\u221F':'angrt','\u2220':'ang','\u2220\u20D2':'nang','\u2221':'angmsd','\u2222':'angsph','\u2223':'mid','\u2224':'nmid','\u2225':'par','\u2226':'npar','\u2227':'and','\u2228':'or','\u2229':'cap','\u2229\uFE00':'caps','\u222A':'cup','\u222A\uFE00':'cups','\u222B':'int','\u222C':'Int','\u222D':'tint','\u2A0C':'qint','\u222E':'oint','\u222F':'Conint','\u2230':'Cconint','\u2231':'cwint','\u2232':'cwconint','\u2233':'awconint','\u2234':'there4','\u2235':'becaus','\u2236':'ratio','\u2237':'Colon','\u2238':'minusd','\u223A':'mDDot','\u223B':'homtht','\u223C':'sim','\u2241':'nsim','\u223C\u20D2':'nvsim','\u223D':'bsim','\u223D\u0331':'race','\u223E':'ac','\u223E\u0333':'acE','\u223F':'acd','\u2240':'wr','\u2242':'esim','\u2242\u0338':'nesim','\u2243':'sime','\u2244':'nsime','\u2245':'cong','\u2247':'ncong','\u2246':'simne','\u2248':'ap','\u2249':'nap','\u224A':'ape','\u224B':'apid','\u224B\u0338':'napid','\u224C':'bcong','\u224D':'CupCap','\u226D':'NotCupCap','\u224D\u20D2':'nvap','\u224E':'bump','\u224E\u0338':'nbump','\u224F':'bumpe','\u224F\u0338':'nbumpe','\u2250':'doteq','\u2250\u0338':'nedot','\u2251':'eDot','\u2252':'efDot','\u2253':'erDot','\u2254':'colone','\u2255':'ecolon','\u2256':'ecir','\u2257':'cire','\u2259':'wedgeq','\u225A':'veeeq','\u225C':'trie','\u225F':'equest','\u2261':'equiv','\u2262':'nequiv','\u2261\u20E5':'bnequiv','\u2264':'le','\u2270':'nle','\u2264\u20D2':'nvle','\u2265':'ge','\u2271':'nge','\u2265\u20D2':'nvge','\u2266':'lE','\u2266\u0338':'nlE','\u2267':'gE','\u2267\u0338':'ngE','\u2268\uFE00':'lvnE','\u2268':'lnE','\u2269':'gnE','\u2269\uFE00':'gvnE','\u226A':'ll','\u226A\u0338':'nLtv','\u226A\u20D2':'nLt','\u226B':'gg','\u226B\u0338':'nGtv','\u226B\u20D2':'nGt','\u226C':'twixt','\u2272':'lsim','\u2274':'nlsim','\u2273':'gsim','\u2275':'ngsim','\u2276':'lg','\u2278':'ntlg','\u2277':'gl','\u2279':'ntgl','\u227A':'pr','\u2280':'npr','\u227B':'sc','\u2281':'nsc','\u227C':'prcue','\u22E0':'nprcue','\u227D':'sccue','\u22E1':'nsccue','\u227E':'prsim','\u227F':'scsim','\u227F\u0338':'NotSucceedsTilde','\u2282':'sub','\u2284':'nsub','\u2282\u20D2':'vnsub','\u2283':'sup','\u2285':'nsup','\u2283\u20D2':'vnsup','\u2286':'sube','\u2288':'nsube','\u2287':'supe','\u2289':'nsupe','\u228A\uFE00':'vsubne','\u228A':'subne','\u228B\uFE00':'vsupne','\u228B':'supne','\u228D':'cupdot','\u228E':'uplus','\u228F':'sqsub','\u228F\u0338':'NotSquareSubset','\u2290':'sqsup','\u2290\u0338':'NotSquareSuperset','\u2291':'sqsube','\u22E2':'nsqsube','\u2292':'sqsupe','\u22E3':'nsqsupe','\u2293':'sqcap','\u2293\uFE00':'sqcaps','\u2294':'sqcup','\u2294\uFE00':'sqcups','\u2295':'oplus','\u2296':'ominus','\u2297':'otimes','\u2298':'osol','\u2299':'odot','\u229A':'ocir','\u229B':'oast','\u229D':'odash','\u229E':'plusb','\u229F':'minusb','\u22A0':'timesb','\u22A1':'sdotb','\u22A2':'vdash','\u22AC':'nvdash','\u22A3':'dashv','\u22A4':'top','\u22A5':'bot','\u22A7':'models','\u22A8':'vDash','\u22AD':'nvDash','\u22A9':'Vdash','\u22AE':'nVdash','\u22AA':'Vvdash','\u22AB':'VDash','\u22AF':'nVDash','\u22B0':'prurel','\u22B2':'vltri','\u22EA':'nltri','\u22B3':'vrtri','\u22EB':'nrtri','\u22B4':'ltrie','\u22EC':'nltrie','\u22B4\u20D2':'nvltrie','\u22B5':'rtrie','\u22ED':'nrtrie','\u22B5\u20D2':'nvrtrie','\u22B6':'origof','\u22B7':'imof','\u22B8':'mumap','\u22B9':'hercon','\u22BA':'intcal','\u22BB':'veebar','\u22BD':'barvee','\u22BE':'angrtvb','\u22BF':'lrtri','\u22C0':'Wedge','\u22C1':'Vee','\u22C2':'xcap','\u22C3':'xcup','\u22C4':'diam','\u22C5':'sdot','\u22C6':'Star','\u22C7':'divonx','\u22C8':'bowtie','\u22C9':'ltimes','\u22CA':'rtimes','\u22CB':'lthree','\u22CC':'rthree','\u22CD':'bsime','\u22CE':'cuvee','\u22CF':'cuwed','\u22D0':'Sub','\u22D1':'Sup','\u22D2':'Cap','\u22D3':'Cup','\u22D4':'fork','\u22D5':'epar','\u22D6':'ltdot','\u22D7':'gtdot','\u22D8':'Ll','\u22D8\u0338':'nLl','\u22D9':'Gg','\u22D9\u0338':'nGg','\u22DA\uFE00':'lesg','\u22DA':'leg','\u22DB':'gel','\u22DB\uFE00':'gesl','\u22DE':'cuepr','\u22DF':'cuesc','\u22E6':'lnsim','\u22E7':'gnsim','\u22E8':'prnsim','\u22E9':'scnsim','\u22EE':'vellip','\u22EF':'ctdot','\u22F0':'utdot','\u22F1':'dtdot','\u22F2':'disin','\u22F3':'isinsv','\u22F4':'isins','\u22F5':'isindot','\u22F5\u0338':'notindot','\u22F6':'notinvc','\u22F7':'notinvb','\u22F9':'isinE','\u22F9\u0338':'notinE','\u22FA':'nisd','\u22FB':'xnis','\u22FC':'nis','\u22FD':'notnivc','\u22FE':'notnivb','\u2305':'barwed','\u2306':'Barwed','\u230C':'drcrop','\u230D':'dlcrop','\u230E':'urcrop','\u230F':'ulcrop','\u2310':'bnot','\u2312':'profline','\u2313':'profsurf','\u2315':'telrec','\u2316':'target','\u231C':'ulcorn','\u231D':'urcorn','\u231E':'dlcorn','\u231F':'drcorn','\u2322':'frown','\u2323':'smile','\u232D':'cylcty','\u232E':'profalar','\u2336':'topbot','\u233D':'ovbar','\u233F':'solbar','\u237C':'angzarr','\u23B0':'lmoust','\u23B1':'rmoust','\u23B4':'tbrk','\u23B5':'bbrk','\u23B6':'bbrktbrk','\u23DC':'OverParenthesis','\u23DD':'UnderParenthesis','\u23DE':'OverBrace','\u23DF':'UnderBrace','\u23E2':'trpezium','\u23E7':'elinters','\u2423':'blank','\u2500':'boxh','\u2502':'boxv','\u250C':'boxdr','\u2510':'boxdl','\u2514':'boxur','\u2518':'boxul','\u251C':'boxvr','\u2524':'boxvl','\u252C':'boxhd','\u2534':'boxhu','\u253C':'boxvh','\u2550':'boxH','\u2551':'boxV','\u2552':'boxdR','\u2553':'boxDr','\u2554':'boxDR','\u2555':'boxdL','\u2556':'boxDl','\u2557':'boxDL','\u2558':'boxuR','\u2559':'boxUr','\u255A':'boxUR','\u255B':'boxuL','\u255C':'boxUl','\u255D':'boxUL','\u255E':'boxvR','\u255F':'boxVr','\u2560':'boxVR','\u2561':'boxvL','\u2562':'boxVl','\u2563':'boxVL','\u2564':'boxHd','\u2565':'boxhD','\u2566':'boxHD','\u2567':'boxHu','\u2568':'boxhU','\u2569':'boxHU','\u256A':'boxvH','\u256B':'boxVh','\u256C':'boxVH','\u2580':'uhblk','\u2584':'lhblk','\u2588':'block','\u2591':'blk14','\u2592':'blk12','\u2593':'blk34','\u25A1':'squ','\u25AA':'squf','\u25AB':'EmptyVerySmallSquare','\u25AD':'rect','\u25AE':'marker','\u25B1':'fltns','\u25B3':'xutri','\u25B4':'utrif','\u25B5':'utri','\u25B8':'rtrif','\u25B9':'rtri','\u25BD':'xdtri','\u25BE':'dtrif','\u25BF':'dtri','\u25C2':'ltrif','\u25C3':'ltri','\u25CA':'loz','\u25CB':'cir','\u25EC':'tridot','\u25EF':'xcirc','\u25F8':'ultri','\u25F9':'urtri','\u25FA':'lltri','\u25FB':'EmptySmallSquare','\u25FC':'FilledSmallSquare','\u2605':'starf','\u2606':'star','\u260E':'phone','\u2640':'female','\u2642':'male','\u2660':'spades','\u2663':'clubs','\u2665':'hearts','\u2666':'diams','\u266A':'sung','\u2713':'check','\u2717':'cross','\u2720':'malt','\u2736':'sext','\u2758':'VerticalSeparator','\u27C8':'bsolhsub','\u27C9':'suphsol','\u27F5':'xlarr','\u27F6':'xrarr','\u27F7':'xharr','\u27F8':'xlArr','\u27F9':'xrArr','\u27FA':'xhArr','\u27FC':'xmap','\u27FF':'dzigrarr','\u2902':'nvlArr','\u2903':'nvrArr','\u2904':'nvHarr','\u2905':'Map','\u290C':'lbarr','\u290D':'rbarr','\u290E':'lBarr','\u290F':'rBarr','\u2910':'RBarr','\u2911':'DDotrahd','\u2912':'UpArrowBar','\u2913':'DownArrowBar','\u2916':'Rarrtl','\u2919':'latail','\u291A':'ratail','\u291B':'lAtail','\u291C':'rAtail','\u291D':'larrfs','\u291E':'rarrfs','\u291F':'larrbfs','\u2920':'rarrbfs','\u2923':'nwarhk','\u2924':'nearhk','\u2925':'searhk','\u2926':'swarhk','\u2927':'nwnear','\u2928':'toea','\u2929':'tosa','\u292A':'swnwar','\u2933':'rarrc','\u2933\u0338':'nrarrc','\u2935':'cudarrr','\u2936':'ldca','\u2937':'rdca','\u2938':'cudarrl','\u2939':'larrpl','\u293C':'curarrm','\u293D':'cularrp','\u2945':'rarrpl','\u2948':'harrcir','\u2949':'Uarrocir','\u294A':'lurdshar','\u294B':'ldrushar','\u294E':'LeftRightVector','\u294F':'RightUpDownVector','\u2950':'DownLeftRightVector','\u2951':'LeftUpDownVector','\u2952':'LeftVectorBar','\u2953':'RightVectorBar','\u2954':'RightUpVectorBar','\u2955':'RightDownVectorBar','\u2956':'DownLeftVectorBar','\u2957':'DownRightVectorBar','\u2958':'LeftUpVectorBar','\u2959':'LeftDownVectorBar','\u295A':'LeftTeeVector','\u295B':'RightTeeVector','\u295C':'RightUpTeeVector','\u295D':'RightDownTeeVector','\u295E':'DownLeftTeeVector','\u295F':'DownRightTeeVector','\u2960':'LeftUpTeeVector','\u2961':'LeftDownTeeVector','\u2962':'lHar','\u2963':'uHar','\u2964':'rHar','\u2965':'dHar','\u2966':'luruhar','\u2967':'ldrdhar','\u2968':'ruluhar','\u2969':'rdldhar','\u296A':'lharul','\u296B':'llhard','\u296C':'rharul','\u296D':'lrhard','\u296E':'udhar','\u296F':'duhar','\u2970':'RoundImplies','\u2971':'erarr','\u2972':'simrarr','\u2973':'larrsim','\u2974':'rarrsim','\u2975':'rarrap','\u2976':'ltlarr','\u2978':'gtrarr','\u2979':'subrarr','\u297B':'suplarr','\u297C':'lfisht','\u297D':'rfisht','\u297E':'ufisht','\u297F':'dfisht','\u299A':'vzigzag','\u299C':'vangrt','\u299D':'angrtvbd','\u29A4':'ange','\u29A5':'range','\u29A6':'dwangle','\u29A7':'uwangle','\u29A8':'angmsdaa','\u29A9':'angmsdab','\u29AA':'angmsdac','\u29AB':'angmsdad','\u29AC':'angmsdae','\u29AD':'angmsdaf','\u29AE':'angmsdag','\u29AF':'angmsdah','\u29B0':'bemptyv','\u29B1':'demptyv','\u29B2':'cemptyv','\u29B3':'raemptyv','\u29B4':'laemptyv','\u29B5':'ohbar','\u29B6':'omid','\u29B7':'opar','\u29B9':'operp','\u29BB':'olcross','\u29BC':'odsold','\u29BE':'olcir','\u29BF':'ofcir','\u29C0':'olt','\u29C1':'ogt','\u29C2':'cirscir','\u29C3':'cirE','\u29C4':'solb','\u29C5':'bsolb','\u29C9':'boxbox','\u29CD':'trisb','\u29CE':'rtriltri','\u29CF':'LeftTriangleBar','\u29CF\u0338':'NotLeftTriangleBar','\u29D0':'RightTriangleBar','\u29D0\u0338':'NotRightTriangleBar','\u29DC':'iinfin','\u29DD':'infintie','\u29DE':'nvinfin','\u29E3':'eparsl','\u29E4':'smeparsl','\u29E5':'eqvparsl','\u29EB':'lozf','\u29F4':'RuleDelayed','\u29F6':'dsol','\u2A00':'xodot','\u2A01':'xoplus','\u2A02':'xotime','\u2A04':'xuplus','\u2A06':'xsqcup','\u2A0D':'fpartint','\u2A10':'cirfnint','\u2A11':'awint','\u2A12':'rppolint','\u2A13':'scpolint','\u2A14':'npolint','\u2A15':'pointint','\u2A16':'quatint','\u2A17':'intlarhk','\u2A22':'pluscir','\u2A23':'plusacir','\u2A24':'simplus','\u2A25':'plusdu','\u2A26':'plussim','\u2A27':'plustwo','\u2A29':'mcomma','\u2A2A':'minusdu','\u2A2D':'loplus','\u2A2E':'roplus','\u2A2F':'Cross','\u2A30':'timesd','\u2A31':'timesbar','\u2A33':'smashp','\u2A34':'lotimes','\u2A35':'rotimes','\u2A36':'otimesas','\u2A37':'Otimes','\u2A38':'odiv','\u2A39':'triplus','\u2A3A':'triminus','\u2A3B':'tritime','\u2A3C':'iprod','\u2A3F':'amalg','\u2A40':'capdot','\u2A42':'ncup','\u2A43':'ncap','\u2A44':'capand','\u2A45':'cupor','\u2A46':'cupcap','\u2A47':'capcup','\u2A48':'cupbrcap','\u2A49':'capbrcup','\u2A4A':'cupcup','\u2A4B':'capcap','\u2A4C':'ccups','\u2A4D':'ccaps','\u2A50':'ccupssm','\u2A53':'And','\u2A54':'Or','\u2A55':'andand','\u2A56':'oror','\u2A57':'orslope','\u2A58':'andslope','\u2A5A':'andv','\u2A5B':'orv','\u2A5C':'andd','\u2A5D':'ord','\u2A5F':'wedbar','\u2A66':'sdote','\u2A6A':'simdot','\u2A6D':'congdot','\u2A6D\u0338':'ncongdot','\u2A6E':'easter','\u2A6F':'apacir','\u2A70':'apE','\u2A70\u0338':'napE','\u2A71':'eplus','\u2A72':'pluse','\u2A73':'Esim','\u2A77':'eDDot','\u2A78':'equivDD','\u2A79':'ltcir','\u2A7A':'gtcir','\u2A7B':'ltquest','\u2A7C':'gtquest','\u2A7D':'les','\u2A7D\u0338':'nles','\u2A7E':'ges','\u2A7E\u0338':'nges','\u2A7F':'lesdot','\u2A80':'gesdot','\u2A81':'lesdoto','\u2A82':'gesdoto','\u2A83':'lesdotor','\u2A84':'gesdotol','\u2A85':'lap','\u2A86':'gap','\u2A87':'lne','\u2A88':'gne','\u2A89':'lnap','\u2A8A':'gnap','\u2A8B':'lEg','\u2A8C':'gEl','\u2A8D':'lsime','\u2A8E':'gsime','\u2A8F':'lsimg','\u2A90':'gsiml','\u2A91':'lgE','\u2A92':'glE','\u2A93':'lesges','\u2A94':'gesles','\u2A95':'els','\u2A96':'egs','\u2A97':'elsdot','\u2A98':'egsdot','\u2A99':'el','\u2A9A':'eg','\u2A9D':'siml','\u2A9E':'simg','\u2A9F':'simlE','\u2AA0':'simgE','\u2AA1':'LessLess','\u2AA1\u0338':'NotNestedLessLess','\u2AA2':'GreaterGreater','\u2AA2\u0338':'NotNestedGreaterGreater','\u2AA4':'glj','\u2AA5':'gla','\u2AA6':'ltcc','\u2AA7':'gtcc','\u2AA8':'lescc','\u2AA9':'gescc','\u2AAA':'smt','\u2AAB':'lat','\u2AAC':'smte','\u2AAC\uFE00':'smtes','\u2AAD':'late','\u2AAD\uFE00':'lates','\u2AAE':'bumpE','\u2AAF':'pre','\u2AAF\u0338':'npre','\u2AB0':'sce','\u2AB0\u0338':'nsce','\u2AB3':'prE','\u2AB4':'scE','\u2AB5':'prnE','\u2AB6':'scnE','\u2AB7':'prap','\u2AB8':'scap','\u2AB9':'prnap','\u2ABA':'scnap','\u2ABB':'Pr','\u2ABC':'Sc','\u2ABD':'subdot','\u2ABE':'supdot','\u2ABF':'subplus','\u2AC0':'supplus','\u2AC1':'submult','\u2AC2':'supmult','\u2AC3':'subedot','\u2AC4':'supedot','\u2AC5':'subE','\u2AC5\u0338':'nsubE','\u2AC6':'supE','\u2AC6\u0338':'nsupE','\u2AC7':'subsim','\u2AC8':'supsim','\u2ACB\uFE00':'vsubnE','\u2ACB':'subnE','\u2ACC\uFE00':'vsupnE','\u2ACC':'supnE','\u2ACF':'csub','\u2AD0':'csup','\u2AD1':'csube','\u2AD2':'csupe','\u2AD3':'subsup','\u2AD4':'supsub','\u2AD5':'subsub','\u2AD6':'supsup','\u2AD7':'suphsub','\u2AD8':'supdsub','\u2AD9':'forkv','\u2ADA':'topfork','\u2ADB':'mlcp','\u2AE4':'Dashv','\u2AE6':'Vdashl','\u2AE7':'Barv','\u2AE8':'vBar','\u2AE9':'vBarv','\u2AEB':'Vbar','\u2AEC':'Not','\u2AED':'bNot','\u2AEE':'rnmid','\u2AEF':'cirmid','\u2AF0':'midcir','\u2AF1':'topcir','\u2AF2':'nhpar','\u2AF3':'parsim','\u2AFD':'parsl','\u2AFD\u20E5':'nparsl','\u266D':'flat','\u266E':'natur','\u266F':'sharp','\xA4':'curren','\xA2':'cent','$':'dollar','\xA3':'pound','\xA5':'yen','\u20AC':'euro','\xB9':'sup1','\xBD':'half','\u2153':'frac13','\xBC':'frac14','\u2155':'frac15','\u2159':'frac16','\u215B':'frac18','\xB2':'sup2','\u2154':'frac23','\u2156':'frac25','\xB3':'sup3','\xBE':'frac34','\u2157':'frac35','\u215C':'frac38','\u2158':'frac45','\u215A':'frac56','\u215D':'frac58','\u215E':'frac78','\uD835\uDCB6':'ascr','\uD835\uDD52':'aopf','\uD835\uDD1E':'afr','\uD835\uDD38':'Aopf','\uD835\uDD04':'Afr','\uD835\uDC9C':'Ascr','\xAA':'ordf','\xE1':'aacute','\xC1':'Aacute','\xE0':'agrave','\xC0':'Agrave','\u0103':'abreve','\u0102':'Abreve','\xE2':'acirc','\xC2':'Acirc','\xE5':'aring','\xC5':'angst','\xE4':'auml','\xC4':'Auml','\xE3':'atilde','\xC3':'Atilde','\u0105':'aogon','\u0104':'Aogon','\u0101':'amacr','\u0100':'Amacr','\xE6':'aelig','\xC6':'AElig','\uD835\uDCB7':'bscr','\uD835\uDD53':'bopf','\uD835\uDD1F':'bfr','\uD835\uDD39':'Bopf','\u212C':'Bscr','\uD835\uDD05':'Bfr','\uD835\uDD20':'cfr','\uD835\uDCB8':'cscr','\uD835\uDD54':'copf','\u212D':'Cfr','\uD835\uDC9E':'Cscr','\u2102':'Copf','\u0107':'cacute','\u0106':'Cacute','\u0109':'ccirc','\u0108':'Ccirc','\u010D':'ccaron','\u010C':'Ccaron','\u010B':'cdot','\u010A':'Cdot','\xE7':'ccedil','\xC7':'Ccedil','\u2105':'incare','\uD835\uDD21':'dfr','\u2146':'dd','\uD835\uDD55':'dopf','\uD835\uDCB9':'dscr','\uD835\uDC9F':'Dscr','\uD835\uDD07':'Dfr','\u2145':'DD','\uD835\uDD3B':'Dopf','\u010F':'dcaron','\u010E':'Dcaron','\u0111':'dstrok','\u0110':'Dstrok','\xF0':'eth','\xD0':'ETH','\u2147':'ee','\u212F':'escr','\uD835\uDD22':'efr','\uD835\uDD56':'eopf','\u2130':'Escr','\uD835\uDD08':'Efr','\uD835\uDD3C':'Eopf','\xE9':'eacute','\xC9':'Eacute','\xE8':'egrave','\xC8':'Egrave','\xEA':'ecirc','\xCA':'Ecirc','\u011B':'ecaron','\u011A':'Ecaron','\xEB':'euml','\xCB':'Euml','\u0117':'edot','\u0116':'Edot','\u0119':'eogon','\u0118':'Eogon','\u0113':'emacr','\u0112':'Emacr','\uD835\uDD23':'ffr','\uD835\uDD57':'fopf','\uD835\uDCBB':'fscr','\uD835\uDD09':'Ffr','\uD835\uDD3D':'Fopf','\u2131':'Fscr','\uFB00':'fflig','\uFB03':'ffilig','\uFB04':'ffllig','\uFB01':'filig','fj':'fjlig','\uFB02':'fllig','\u0192':'fnof','\u210A':'gscr','\uD835\uDD58':'gopf','\uD835\uDD24':'gfr','\uD835\uDCA2':'Gscr','\uD835\uDD3E':'Gopf','\uD835\uDD0A':'Gfr','\u01F5':'gacute','\u011F':'gbreve','\u011E':'Gbreve','\u011D':'gcirc','\u011C':'Gcirc','\u0121':'gdot','\u0120':'Gdot','\u0122':'Gcedil','\uD835\uDD25':'hfr','\u210E':'planckh','\uD835\uDCBD':'hscr','\uD835\uDD59':'hopf','\u210B':'Hscr','\u210C':'Hfr','\u210D':'Hopf','\u0125':'hcirc','\u0124':'Hcirc','\u210F':'hbar','\u0127':'hstrok','\u0126':'Hstrok','\uD835\uDD5A':'iopf','\uD835\uDD26':'ifr','\uD835\uDCBE':'iscr','\u2148':'ii','\uD835\uDD40':'Iopf','\u2110':'Iscr','\u2111':'Im','\xED':'iacute','\xCD':'Iacute','\xEC':'igrave','\xCC':'Igrave','\xEE':'icirc','\xCE':'Icirc','\xEF':'iuml','\xCF':'Iuml','\u0129':'itilde','\u0128':'Itilde','\u0130':'Idot','\u012F':'iogon','\u012E':'Iogon','\u012B':'imacr','\u012A':'Imacr','\u0133':'ijlig','\u0132':'IJlig','\u0131':'imath','\uD835\uDCBF':'jscr','\uD835\uDD5B':'jopf','\uD835\uDD27':'jfr','\uD835\uDCA5':'Jscr','\uD835\uDD0D':'Jfr','\uD835\uDD41':'Jopf','\u0135':'jcirc','\u0134':'Jcirc','\u0237':'jmath','\uD835\uDD5C':'kopf','\uD835\uDCC0':'kscr','\uD835\uDD28':'kfr','\uD835\uDCA6':'Kscr','\uD835\uDD42':'Kopf','\uD835\uDD0E':'Kfr','\u0137':'kcedil','\u0136':'Kcedil','\uD835\uDD29':'lfr','\uD835\uDCC1':'lscr','\u2113':'ell','\uD835\uDD5D':'lopf','\u2112':'Lscr','\uD835\uDD0F':'Lfr','\uD835\uDD43':'Lopf','\u013A':'lacute','\u0139':'Lacute','\u013E':'lcaron','\u013D':'Lcaron','\u013C':'lcedil','\u013B':'Lcedil','\u0142':'lstrok','\u0141':'Lstrok','\u0140':'lmidot','\u013F':'Lmidot','\uD835\uDD2A':'mfr','\uD835\uDD5E':'mopf','\uD835\uDCC2':'mscr','\uD835\uDD10':'Mfr','\uD835\uDD44':'Mopf','\u2133':'Mscr','\uD835\uDD2B':'nfr','\uD835\uDD5F':'nopf','\uD835\uDCC3':'nscr','\u2115':'Nopf','\uD835\uDCA9':'Nscr','\uD835\uDD11':'Nfr','\u0144':'nacute','\u0143':'Nacute','\u0148':'ncaron','\u0147':'Ncaron','\xF1':'ntilde','\xD1':'Ntilde','\u0146':'ncedil','\u0145':'Ncedil','\u2116':'numero','\u014B':'eng','\u014A':'ENG','\uD835\uDD60':'oopf','\uD835\uDD2C':'ofr','\u2134':'oscr','\uD835\uDCAA':'Oscr','\uD835\uDD12':'Ofr','\uD835\uDD46':'Oopf','\xBA':'ordm','\xF3':'oacute','\xD3':'Oacute','\xF2':'ograve','\xD2':'Ograve','\xF4':'ocirc','\xD4':'Ocirc','\xF6':'ouml','\xD6':'Ouml','\u0151':'odblac','\u0150':'Odblac','\xF5':'otilde','\xD5':'Otilde','\xF8':'oslash','\xD8':'Oslash','\u014D':'omacr','\u014C':'Omacr','\u0153':'oelig','\u0152':'OElig','\uD835\uDD2D':'pfr','\uD835\uDCC5':'pscr','\uD835\uDD61':'popf','\u2119':'Popf','\uD835\uDD13':'Pfr','\uD835\uDCAB':'Pscr','\uD835\uDD62':'qopf','\uD835\uDD2E':'qfr','\uD835\uDCC6':'qscr','\uD835\uDCAC':'Qscr','\uD835\uDD14':'Qfr','\u211A':'Qopf','\u0138':'kgreen','\uD835\uDD2F':'rfr','\uD835\uDD63':'ropf','\uD835\uDCC7':'rscr','\u211B':'Rscr','\u211C':'Re','\u211D':'Ropf','\u0155':'racute','\u0154':'Racute','\u0159':'rcaron','\u0158':'Rcaron','\u0157':'rcedil','\u0156':'Rcedil','\uD835\uDD64':'sopf','\uD835\uDCC8':'sscr','\uD835\uDD30':'sfr','\uD835\uDD4A':'Sopf','\uD835\uDD16':'Sfr','\uD835\uDCAE':'Sscr','\u24C8':'oS','\u015B':'sacute','\u015A':'Sacute','\u015D':'scirc','\u015C':'Scirc','\u0161':'scaron','\u0160':'Scaron','\u015F':'scedil','\u015E':'Scedil','\xDF':'szlig','\uD835\uDD31':'tfr','\uD835\uDCC9':'tscr','\uD835\uDD65':'topf','\uD835\uDCAF':'Tscr','\uD835\uDD17':'Tfr','\uD835\uDD4B':'Topf','\u0165':'tcaron','\u0164':'Tcaron','\u0163':'tcedil','\u0162':'Tcedil','\u2122':'trade','\u0167':'tstrok','\u0166':'Tstrok','\uD835\uDCCA':'uscr','\uD835\uDD66':'uopf','\uD835\uDD32':'ufr','\uD835\uDD4C':'Uopf','\uD835\uDD18':'Ufr','\uD835\uDCB0':'Uscr','\xFA':'uacute','\xDA':'Uacute','\xF9':'ugrave','\xD9':'Ugrave','\u016D':'ubreve','\u016C':'Ubreve','\xFB':'ucirc','\xDB':'Ucirc','\u016F':'uring','\u016E':'Uring','\xFC':'uuml','\xDC':'Uuml','\u0171':'udblac','\u0170':'Udblac','\u0169':'utilde','\u0168':'Utilde','\u0173':'uogon','\u0172':'Uogon','\u016B':'umacr','\u016A':'Umacr','\uD835\uDD33':'vfr','\uD835\uDD67':'vopf','\uD835\uDCCB':'vscr','\uD835\uDD19':'Vfr','\uD835\uDD4D':'Vopf','\uD835\uDCB1':'Vscr','\uD835\uDD68':'wopf','\uD835\uDCCC':'wscr','\uD835\uDD34':'wfr','\uD835\uDCB2':'Wscr','\uD835\uDD4E':'Wopf','\uD835\uDD1A':'Wfr','\u0175':'wcirc','\u0174':'Wcirc','\uD835\uDD35':'xfr','\uD835\uDCCD':'xscr','\uD835\uDD69':'xopf','\uD835\uDD4F':'Xopf','\uD835\uDD1B':'Xfr','\uD835\uDCB3':'Xscr','\uD835\uDD36':'yfr','\uD835\uDCCE':'yscr','\uD835\uDD6A':'yopf','\uD835\uDCB4':'Yscr','\uD835\uDD1C':'Yfr','\uD835\uDD50':'Yopf','\xFD':'yacute','\xDD':'Yacute','\u0177':'ycirc','\u0176':'Ycirc','\xFF':'yuml','\u0178':'Yuml','\uD835\uDCCF':'zscr','\uD835\uDD37':'zfr','\uD835\uDD6B':'zopf','\u2128':'Zfr','\u2124':'Zopf','\uD835\uDCB5':'Zscr','\u017A':'zacute','\u0179':'Zacute','\u017E':'zcaron','\u017D':'Zcaron','\u017C':'zdot','\u017B':'Zdot','\u01B5':'imped','\xFE':'thorn','\xDE':'THORN','\u0149':'napos','\u03B1':'alpha','\u0391':'Alpha','\u03B2':'beta','\u0392':'Beta','\u03B3':'gamma','\u0393':'Gamma','\u03B4':'delta','\u0394':'Delta','\u03B5':'epsi','\u03F5':'epsiv','\u0395':'Epsilon','\u03DD':'gammad','\u03DC':'Gammad','\u03B6':'zeta','\u0396':'Zeta','\u03B7':'eta','\u0397':'Eta','\u03B8':'theta','\u03D1':'thetav','\u0398':'Theta','\u03B9':'iota','\u0399':'Iota','\u03BA':'kappa','\u03F0':'kappav','\u039A':'Kappa','\u03BB':'lambda','\u039B':'Lambda','\u03BC':'mu','\xB5':'micro','\u039C':'Mu','\u03BD':'nu','\u039D':'Nu','\u03BE':'xi','\u039E':'Xi','\u03BF':'omicron','\u039F':'Omicron','\u03C0':'pi','\u03D6':'piv','\u03A0':'Pi','\u03C1':'rho','\u03F1':'rhov','\u03A1':'Rho','\u03C3':'sigma','\u03A3':'Sigma','\u03C2':'sigmaf','\u03C4':'tau','\u03A4':'Tau','\u03C5':'upsi','\u03A5':'Upsilon','\u03D2':'Upsi','\u03C6':'phi','\u03D5':'phiv','\u03A6':'Phi','\u03C7':'chi','\u03A7':'Chi','\u03C8':'psi','\u03A8':'Psi','\u03C9':'omega','\u03A9':'ohm','\u0430':'acy','\u0410':'Acy','\u0431':'bcy','\u0411':'Bcy','\u0432':'vcy','\u0412':'Vcy','\u0433':'gcy','\u0413':'Gcy','\u0453':'gjcy','\u0403':'GJcy','\u0434':'dcy','\u0414':'Dcy','\u0452':'djcy','\u0402':'DJcy','\u0435':'iecy','\u0415':'IEcy','\u0451':'iocy','\u0401':'IOcy','\u0454':'jukcy','\u0404':'Jukcy','\u0436':'zhcy','\u0416':'ZHcy','\u0437':'zcy','\u0417':'Zcy','\u0455':'dscy','\u0405':'DScy','\u0438':'icy','\u0418':'Icy','\u0456':'iukcy','\u0406':'Iukcy','\u0457':'yicy','\u0407':'YIcy','\u0439':'jcy','\u0419':'Jcy','\u0458':'jsercy','\u0408':'Jsercy','\u043A':'kcy','\u041A':'Kcy','\u045C':'kjcy','\u040C':'KJcy','\u043B':'lcy','\u041B':'Lcy','\u0459':'ljcy','\u0409':'LJcy','\u043C':'mcy','\u041C':'Mcy','\u043D':'ncy','\u041D':'Ncy','\u045A':'njcy','\u040A':'NJcy','\u043E':'ocy','\u041E':'Ocy','\u043F':'pcy','\u041F':'Pcy','\u0440':'rcy','\u0420':'Rcy','\u0441':'scy','\u0421':'Scy','\u0442':'tcy','\u0422':'Tcy','\u045B':'tshcy','\u040B':'TSHcy','\u0443':'ucy','\u0423':'Ucy','\u045E':'ubrcy','\u040E':'Ubrcy','\u0444':'fcy','\u0424':'Fcy','\u0445':'khcy','\u0425':'KHcy','\u0446':'tscy','\u0426':'TScy','\u0447':'chcy','\u0427':'CHcy','\u045F':'dzcy','\u040F':'DZcy','\u0448':'shcy','\u0428':'SHcy','\u0449':'shchcy','\u0429':'SHCHcy','\u044A':'hardcy','\u042A':'HARDcy','\u044B':'ycy','\u042B':'Ycy','\u044C':'softcy','\u042C':'SOFTcy','\u044D':'ecy','\u042D':'Ecy','\u044E':'yucy','\u042E':'YUcy','\u044F':'yacy','\u042F':'YAcy','\u2135':'aleph','\u2136':'beth','\u2137':'gimel','\u2138':'daleth'};

        var regexEscape = /["&'<>`]/g;
        var escapeMap = {
            '"': '&quot;',
            '&': '&amp;',
            '\'': '&#x27;',
            '<': '&lt;',
            // See https://mathiasbynens.be/notes/ambiguous-ampersands: in HTML, the
            // following is not strictly necessary unless it’s part of a tag or an
            // unquoted attribute value. We’re only escaping it to support those
            // situations, and for XML support.
            '>': '&gt;',
            // In Internet Explorer ≤ 8, the backtick character can be used
            // to break out of (un)quoted attribute values or HTML comments.
            // See http://html5sec.org/#102, http://html5sec.org/#108, and
            // http://html5sec.org/#133.
            '`': '&#x60;'
        };

        var regexInvalidEntity = /&#(?:[xX][^a-fA-F0-9]|[^0-9xX])/;
        var regexInvalidRawCodePoint = /[\0-\x08\x0B\x0E-\x1F\x7F-\x9F\uFDD0-\uFDEF\uFFFE\uFFFF]|[\uD83F\uD87F\uD8BF\uD8FF\uD93F\uD97F\uD9BF\uD9FF\uDA3F\uDA7F\uDABF\uDAFF\uDB3F\uDB7F\uDBBF\uDBFF][\uDFFE\uDFFF]|[\uD800-\uDBFF](?![\uDC00-\uDFFF])|(?:[^\uD800-\uDBFF]|^)[\uDC00-\uDFFF]/;
        var regexDecode = /&#([0-9]+)(;?)|&#[xX]([a-fA-F0-9]+)(;?)|&([0-9a-zA-Z]+);|&(Aacute|Agrave|Atilde|Ccedil|Eacute|Egrave|Iacute|Igrave|Ntilde|Oacute|Ograve|Oslash|Otilde|Uacute|Ugrave|Yacute|aacute|agrave|atilde|brvbar|ccedil|curren|divide|eacute|egrave|frac12|frac14|frac34|iacute|igrave|iquest|middot|ntilde|oacute|ograve|oslash|otilde|plusmn|uacute|ugrave|yacute|AElig|Acirc|Aring|Ecirc|Icirc|Ocirc|THORN|Ucirc|acirc|acute|aelig|aring|cedil|ecirc|icirc|iexcl|laquo|micro|ocirc|pound|raquo|szlig|thorn|times|ucirc|Auml|COPY|Euml|Iuml|Ouml|QUOT|Uuml|auml|cent|copy|euml|iuml|macr|nbsp|ordf|ordm|ouml|para|quot|sect|sup1|sup2|sup3|uuml|yuml|AMP|ETH|REG|amp|deg|eth|not|reg|shy|uml|yen|GT|LT|gt|lt)([=a-zA-Z0-9])?/g;
        var decodeMap = {'aacute':'\xE1','Aacute':'\xC1','abreve':'\u0103','Abreve':'\u0102','ac':'\u223E','acd':'\u223F','acE':'\u223E\u0333','acirc':'\xE2','Acirc':'\xC2','acute':'\xB4','acy':'\u0430','Acy':'\u0410','aelig':'\xE6','AElig':'\xC6','af':'\u2061','afr':'\uD835\uDD1E','Afr':'\uD835\uDD04','agrave':'\xE0','Agrave':'\xC0','alefsym':'\u2135','aleph':'\u2135','alpha':'\u03B1','Alpha':'\u0391','amacr':'\u0101','Amacr':'\u0100','amalg':'\u2A3F','amp':'&','AMP':'&','and':'\u2227','And':'\u2A53','andand':'\u2A55','andd':'\u2A5C','andslope':'\u2A58','andv':'\u2A5A','ang':'\u2220','ange':'\u29A4','angle':'\u2220','angmsd':'\u2221','angmsdaa':'\u29A8','angmsdab':'\u29A9','angmsdac':'\u29AA','angmsdad':'\u29AB','angmsdae':'\u29AC','angmsdaf':'\u29AD','angmsdag':'\u29AE','angmsdah':'\u29AF','angrt':'\u221F','angrtvb':'\u22BE','angrtvbd':'\u299D','angsph':'\u2222','angst':'\xC5','angzarr':'\u237C','aogon':'\u0105','Aogon':'\u0104','aopf':'\uD835\uDD52','Aopf':'\uD835\uDD38','ap':'\u2248','apacir':'\u2A6F','ape':'\u224A','apE':'\u2A70','apid':'\u224B','apos':'\'','ApplyFunction':'\u2061','approx':'\u2248','approxeq':'\u224A','aring':'\xE5','Aring':'\xC5','ascr':'\uD835\uDCB6','Ascr':'\uD835\uDC9C','Assign':'\u2254','ast':'*','asymp':'\u2248','asympeq':'\u224D','atilde':'\xE3','Atilde':'\xC3','auml':'\xE4','Auml':'\xC4','awconint':'\u2233','awint':'\u2A11','backcong':'\u224C','backepsilon':'\u03F6','backprime':'\u2035','backsim':'\u223D','backsimeq':'\u22CD','Backslash':'\u2216','Barv':'\u2AE7','barvee':'\u22BD','barwed':'\u2305','Barwed':'\u2306','barwedge':'\u2305','bbrk':'\u23B5','bbrktbrk':'\u23B6','bcong':'\u224C','bcy':'\u0431','Bcy':'\u0411','bdquo':'\u201E','becaus':'\u2235','because':'\u2235','Because':'\u2235','bemptyv':'\u29B0','bepsi':'\u03F6','bernou':'\u212C','Bernoullis':'\u212C','beta':'\u03B2','Beta':'\u0392','beth':'\u2136','between':'\u226C','bfr':'\uD835\uDD1F','Bfr':'\uD835\uDD05','bigcap':'\u22C2','bigcirc':'\u25EF','bigcup':'\u22C3','bigodot':'\u2A00','bigoplus':'\u2A01','bigotimes':'\u2A02','bigsqcup':'\u2A06','bigstar':'\u2605','bigtriangledown':'\u25BD','bigtriangleup':'\u25B3','biguplus':'\u2A04','bigvee':'\u22C1','bigwedge':'\u22C0','bkarow':'\u290D','blacklozenge':'\u29EB','blacksquare':'\u25AA','blacktriangle':'\u25B4','blacktriangledown':'\u25BE','blacktriangleleft':'\u25C2','blacktriangleright':'\u25B8','blank':'\u2423','blk12':'\u2592','blk14':'\u2591','blk34':'\u2593','block':'\u2588','bne':'=\u20E5','bnequiv':'\u2261\u20E5','bnot':'\u2310','bNot':'\u2AED','bopf':'\uD835\uDD53','Bopf':'\uD835\uDD39','bot':'\u22A5','bottom':'\u22A5','bowtie':'\u22C8','boxbox':'\u29C9','boxdl':'\u2510','boxdL':'\u2555','boxDl':'\u2556','boxDL':'\u2557','boxdr':'\u250C','boxdR':'\u2552','boxDr':'\u2553','boxDR':'\u2554','boxh':'\u2500','boxH':'\u2550','boxhd':'\u252C','boxhD':'\u2565','boxHd':'\u2564','boxHD':'\u2566','boxhu':'\u2534','boxhU':'\u2568','boxHu':'\u2567','boxHU':'\u2569','boxminus':'\u229F','boxplus':'\u229E','boxtimes':'\u22A0','boxul':'\u2518','boxuL':'\u255B','boxUl':'\u255C','boxUL':'\u255D','boxur':'\u2514','boxuR':'\u2558','boxUr':'\u2559','boxUR':'\u255A','boxv':'\u2502','boxV':'\u2551','boxvh':'\u253C','boxvH':'\u256A','boxVh':'\u256B','boxVH':'\u256C','boxvl':'\u2524','boxvL':'\u2561','boxVl':'\u2562','boxVL':'\u2563','boxvr':'\u251C','boxvR':'\u255E','boxVr':'\u255F','boxVR':'\u2560','bprime':'\u2035','breve':'\u02D8','Breve':'\u02D8','brvbar':'\xA6','bscr':'\uD835\uDCB7','Bscr':'\u212C','bsemi':'\u204F','bsim':'\u223D','bsime':'\u22CD','bsol':'\\','bsolb':'\u29C5','bsolhsub':'\u27C8','bull':'\u2022','bullet':'\u2022','bump':'\u224E','bumpe':'\u224F','bumpE':'\u2AAE','bumpeq':'\u224F','Bumpeq':'\u224E','cacute':'\u0107','Cacute':'\u0106','cap':'\u2229','Cap':'\u22D2','capand':'\u2A44','capbrcup':'\u2A49','capcap':'\u2A4B','capcup':'\u2A47','capdot':'\u2A40','CapitalDifferentialD':'\u2145','caps':'\u2229\uFE00','caret':'\u2041','caron':'\u02C7','Cayleys':'\u212D','ccaps':'\u2A4D','ccaron':'\u010D','Ccaron':'\u010C','ccedil':'\xE7','Ccedil':'\xC7','ccirc':'\u0109','Ccirc':'\u0108','Cconint':'\u2230','ccups':'\u2A4C','ccupssm':'\u2A50','cdot':'\u010B','Cdot':'\u010A','cedil':'\xB8','Cedilla':'\xB8','cemptyv':'\u29B2','cent':'\xA2','centerdot':'\xB7','CenterDot':'\xB7','cfr':'\uD835\uDD20','Cfr':'\u212D','chcy':'\u0447','CHcy':'\u0427','check':'\u2713','checkmark':'\u2713','chi':'\u03C7','Chi':'\u03A7','cir':'\u25CB','circ':'\u02C6','circeq':'\u2257','circlearrowleft':'\u21BA','circlearrowright':'\u21BB','circledast':'\u229B','circledcirc':'\u229A','circleddash':'\u229D','CircleDot':'\u2299','circledR':'\xAE','circledS':'\u24C8','CircleMinus':'\u2296','CirclePlus':'\u2295','CircleTimes':'\u2297','cire':'\u2257','cirE':'\u29C3','cirfnint':'\u2A10','cirmid':'\u2AEF','cirscir':'\u29C2','ClockwiseContourIntegral':'\u2232','CloseCurlyDoubleQuote':'\u201D','CloseCurlyQuote':'\u2019','clubs':'\u2663','clubsuit':'\u2663','colon':':','Colon':'\u2237','colone':'\u2254','Colone':'\u2A74','coloneq':'\u2254','comma':',','commat':'@','comp':'\u2201','compfn':'\u2218','complement':'\u2201','complexes':'\u2102','cong':'\u2245','congdot':'\u2A6D','Congruent':'\u2261','conint':'\u222E','Conint':'\u222F','ContourIntegral':'\u222E','copf':'\uD835\uDD54','Copf':'\u2102','coprod':'\u2210','Coproduct':'\u2210','copy':'\xA9','COPY':'\xA9','copysr':'\u2117','CounterClockwiseContourIntegral':'\u2233','crarr':'\u21B5','cross':'\u2717','Cross':'\u2A2F','cscr':'\uD835\uDCB8','Cscr':'\uD835\uDC9E','csub':'\u2ACF','csube':'\u2AD1','csup':'\u2AD0','csupe':'\u2AD2','ctdot':'\u22EF','cudarrl':'\u2938','cudarrr':'\u2935','cuepr':'\u22DE','cuesc':'\u22DF','cularr':'\u21B6','cularrp':'\u293D','cup':'\u222A','Cup':'\u22D3','cupbrcap':'\u2A48','cupcap':'\u2A46','CupCap':'\u224D','cupcup':'\u2A4A','cupdot':'\u228D','cupor':'\u2A45','cups':'\u222A\uFE00','curarr':'\u21B7','curarrm':'\u293C','curlyeqprec':'\u22DE','curlyeqsucc':'\u22DF','curlyvee':'\u22CE','curlywedge':'\u22CF','curren':'\xA4','curvearrowleft':'\u21B6','curvearrowright':'\u21B7','cuvee':'\u22CE','cuwed':'\u22CF','cwconint':'\u2232','cwint':'\u2231','cylcty':'\u232D','dagger':'\u2020','Dagger':'\u2021','daleth':'\u2138','darr':'\u2193','dArr':'\u21D3','Darr':'\u21A1','dash':'\u2010','dashv':'\u22A3','Dashv':'\u2AE4','dbkarow':'\u290F','dblac':'\u02DD','dcaron':'\u010F','Dcaron':'\u010E','dcy':'\u0434','Dcy':'\u0414','dd':'\u2146','DD':'\u2145','ddagger':'\u2021','ddarr':'\u21CA','DDotrahd':'\u2911','ddotseq':'\u2A77','deg':'\xB0','Del':'\u2207','delta':'\u03B4','Delta':'\u0394','demptyv':'\u29B1','dfisht':'\u297F','dfr':'\uD835\uDD21','Dfr':'\uD835\uDD07','dHar':'\u2965','dharl':'\u21C3','dharr':'\u21C2','DiacriticalAcute':'\xB4','DiacriticalDot':'\u02D9','DiacriticalDoubleAcute':'\u02DD','DiacriticalGrave':'`','DiacriticalTilde':'\u02DC','diam':'\u22C4','diamond':'\u22C4','Diamond':'\u22C4','diamondsuit':'\u2666','diams':'\u2666','die':'\xA8','DifferentialD':'\u2146','digamma':'\u03DD','disin':'\u22F2','div':'\xF7','divide':'\xF7','divideontimes':'\u22C7','divonx':'\u22C7','djcy':'\u0452','DJcy':'\u0402','dlcorn':'\u231E','dlcrop':'\u230D','dollar':'$','dopf':'\uD835\uDD55','Dopf':'\uD835\uDD3B','dot':'\u02D9','Dot':'\xA8','DotDot':'\u20DC','doteq':'\u2250','doteqdot':'\u2251','DotEqual':'\u2250','dotminus':'\u2238','dotplus':'\u2214','dotsquare':'\u22A1','doublebarwedge':'\u2306','DoubleContourIntegral':'\u222F','DoubleDot':'\xA8','DoubleDownArrow':'\u21D3','DoubleLeftArrow':'\u21D0','DoubleLeftRightArrow':'\u21D4','DoubleLeftTee':'\u2AE4','DoubleLongLeftArrow':'\u27F8','DoubleLongLeftRightArrow':'\u27FA','DoubleLongRightArrow':'\u27F9','DoubleRightArrow':'\u21D2','DoubleRightTee':'\u22A8','DoubleUpArrow':'\u21D1','DoubleUpDownArrow':'\u21D5','DoubleVerticalBar':'\u2225','downarrow':'\u2193','Downarrow':'\u21D3','DownArrow':'\u2193','DownArrowBar':'\u2913','DownArrowUpArrow':'\u21F5','DownBreve':'\u0311','downdownarrows':'\u21CA','downharpoonleft':'\u21C3','downharpoonright':'\u21C2','DownLeftRightVector':'\u2950','DownLeftTeeVector':'\u295E','DownLeftVector':'\u21BD','DownLeftVectorBar':'\u2956','DownRightTeeVector':'\u295F','DownRightVector':'\u21C1','DownRightVectorBar':'\u2957','DownTee':'\u22A4','DownTeeArrow':'\u21A7','drbkarow':'\u2910','drcorn':'\u231F','drcrop':'\u230C','dscr':'\uD835\uDCB9','Dscr':'\uD835\uDC9F','dscy':'\u0455','DScy':'\u0405','dsol':'\u29F6','dstrok':'\u0111','Dstrok':'\u0110','dtdot':'\u22F1','dtri':'\u25BF','dtrif':'\u25BE','duarr':'\u21F5','duhar':'\u296F','dwangle':'\u29A6','dzcy':'\u045F','DZcy':'\u040F','dzigrarr':'\u27FF','eacute':'\xE9','Eacute':'\xC9','easter':'\u2A6E','ecaron':'\u011B','Ecaron':'\u011A','ecir':'\u2256','ecirc':'\xEA','Ecirc':'\xCA','ecolon':'\u2255','ecy':'\u044D','Ecy':'\u042D','eDDot':'\u2A77','edot':'\u0117','eDot':'\u2251','Edot':'\u0116','ee':'\u2147','efDot':'\u2252','efr':'\uD835\uDD22','Efr':'\uD835\uDD08','eg':'\u2A9A','egrave':'\xE8','Egrave':'\xC8','egs':'\u2A96','egsdot':'\u2A98','el':'\u2A99','Element':'\u2208','elinters':'\u23E7','ell':'\u2113','els':'\u2A95','elsdot':'\u2A97','emacr':'\u0113','Emacr':'\u0112','empty':'\u2205','emptyset':'\u2205','EmptySmallSquare':'\u25FB','emptyv':'\u2205','EmptyVerySmallSquare':'\u25AB','emsp':'\u2003','emsp13':'\u2004','emsp14':'\u2005','eng':'\u014B','ENG':'\u014A','ensp':'\u2002','eogon':'\u0119','Eogon':'\u0118','eopf':'\uD835\uDD56','Eopf':'\uD835\uDD3C','epar':'\u22D5','eparsl':'\u29E3','eplus':'\u2A71','epsi':'\u03B5','epsilon':'\u03B5','Epsilon':'\u0395','epsiv':'\u03F5','eqcirc':'\u2256','eqcolon':'\u2255','eqsim':'\u2242','eqslantgtr':'\u2A96','eqslantless':'\u2A95','Equal':'\u2A75','equals':'=','EqualTilde':'\u2242','equest':'\u225F','Equilibrium':'\u21CC','equiv':'\u2261','equivDD':'\u2A78','eqvparsl':'\u29E5','erarr':'\u2971','erDot':'\u2253','escr':'\u212F','Escr':'\u2130','esdot':'\u2250','esim':'\u2242','Esim':'\u2A73','eta':'\u03B7','Eta':'\u0397','eth':'\xF0','ETH':'\xD0','euml':'\xEB','Euml':'\xCB','euro':'\u20AC','excl':'!','exist':'\u2203','Exists':'\u2203','expectation':'\u2130','exponentiale':'\u2147','ExponentialE':'\u2147','fallingdotseq':'\u2252','fcy':'\u0444','Fcy':'\u0424','female':'\u2640','ffilig':'\uFB03','fflig':'\uFB00','ffllig':'\uFB04','ffr':'\uD835\uDD23','Ffr':'\uD835\uDD09','filig':'\uFB01','FilledSmallSquare':'\u25FC','FilledVerySmallSquare':'\u25AA','fjlig':'fj','flat':'\u266D','fllig':'\uFB02','fltns':'\u25B1','fnof':'\u0192','fopf':'\uD835\uDD57','Fopf':'\uD835\uDD3D','forall':'\u2200','ForAll':'\u2200','fork':'\u22D4','forkv':'\u2AD9','Fouriertrf':'\u2131','fpartint':'\u2A0D','frac12':'\xBD','frac13':'\u2153','frac14':'\xBC','frac15':'\u2155','frac16':'\u2159','frac18':'\u215B','frac23':'\u2154','frac25':'\u2156','frac34':'\xBE','frac35':'\u2157','frac38':'\u215C','frac45':'\u2158','frac56':'\u215A','frac58':'\u215D','frac78':'\u215E','frasl':'\u2044','frown':'\u2322','fscr':'\uD835\uDCBB','Fscr':'\u2131','gacute':'\u01F5','gamma':'\u03B3','Gamma':'\u0393','gammad':'\u03DD','Gammad':'\u03DC','gap':'\u2A86','gbreve':'\u011F','Gbreve':'\u011E','Gcedil':'\u0122','gcirc':'\u011D','Gcirc':'\u011C','gcy':'\u0433','Gcy':'\u0413','gdot':'\u0121','Gdot':'\u0120','ge':'\u2265','gE':'\u2267','gel':'\u22DB','gEl':'\u2A8C','geq':'\u2265','geqq':'\u2267','geqslant':'\u2A7E','ges':'\u2A7E','gescc':'\u2AA9','gesdot':'\u2A80','gesdoto':'\u2A82','gesdotol':'\u2A84','gesl':'\u22DB\uFE00','gesles':'\u2A94','gfr':'\uD835\uDD24','Gfr':'\uD835\uDD0A','gg':'\u226B','Gg':'\u22D9','ggg':'\u22D9','gimel':'\u2137','gjcy':'\u0453','GJcy':'\u0403','gl':'\u2277','gla':'\u2AA5','glE':'\u2A92','glj':'\u2AA4','gnap':'\u2A8A','gnapprox':'\u2A8A','gne':'\u2A88','gnE':'\u2269','gneq':'\u2A88','gneqq':'\u2269','gnsim':'\u22E7','gopf':'\uD835\uDD58','Gopf':'\uD835\uDD3E','grave':'`','GreaterEqual':'\u2265','GreaterEqualLess':'\u22DB','GreaterFullEqual':'\u2267','GreaterGreater':'\u2AA2','GreaterLess':'\u2277','GreaterSlantEqual':'\u2A7E','GreaterTilde':'\u2273','gscr':'\u210A','Gscr':'\uD835\uDCA2','gsim':'\u2273','gsime':'\u2A8E','gsiml':'\u2A90','gt':'>','Gt':'\u226B','GT':'>','gtcc':'\u2AA7','gtcir':'\u2A7A','gtdot':'\u22D7','gtlPar':'\u2995','gtquest':'\u2A7C','gtrapprox':'\u2A86','gtrarr':'\u2978','gtrdot':'\u22D7','gtreqless':'\u22DB','gtreqqless':'\u2A8C','gtrless':'\u2277','gtrsim':'\u2273','gvertneqq':'\u2269\uFE00','gvnE':'\u2269\uFE00','Hacek':'\u02C7','hairsp':'\u200A','half':'\xBD','hamilt':'\u210B','hardcy':'\u044A','HARDcy':'\u042A','harr':'\u2194','hArr':'\u21D4','harrcir':'\u2948','harrw':'\u21AD','Hat':'^','hbar':'\u210F','hcirc':'\u0125','Hcirc':'\u0124','hearts':'\u2665','heartsuit':'\u2665','hellip':'\u2026','hercon':'\u22B9','hfr':'\uD835\uDD25','Hfr':'\u210C','HilbertSpace':'\u210B','hksearow':'\u2925','hkswarow':'\u2926','hoarr':'\u21FF','homtht':'\u223B','hookleftarrow':'\u21A9','hookrightarrow':'\u21AA','hopf':'\uD835\uDD59','Hopf':'\u210D','horbar':'\u2015','HorizontalLine':'\u2500','hscr':'\uD835\uDCBD','Hscr':'\u210B','hslash':'\u210F','hstrok':'\u0127','Hstrok':'\u0126','HumpDownHump':'\u224E','HumpEqual':'\u224F','hybull':'\u2043','hyphen':'\u2010','iacute':'\xED','Iacute':'\xCD','ic':'\u2063','icirc':'\xEE','Icirc':'\xCE','icy':'\u0438','Icy':'\u0418','Idot':'\u0130','iecy':'\u0435','IEcy':'\u0415','iexcl':'\xA1','iff':'\u21D4','ifr':'\uD835\uDD26','Ifr':'\u2111','igrave':'\xEC','Igrave':'\xCC','ii':'\u2148','iiiint':'\u2A0C','iiint':'\u222D','iinfin':'\u29DC','iiota':'\u2129','ijlig':'\u0133','IJlig':'\u0132','Im':'\u2111','imacr':'\u012B','Imacr':'\u012A','image':'\u2111','ImaginaryI':'\u2148','imagline':'\u2110','imagpart':'\u2111','imath':'\u0131','imof':'\u22B7','imped':'\u01B5','Implies':'\u21D2','in':'\u2208','incare':'\u2105','infin':'\u221E','infintie':'\u29DD','inodot':'\u0131','int':'\u222B','Int':'\u222C','intcal':'\u22BA','integers':'\u2124','Integral':'\u222B','intercal':'\u22BA','Intersection':'\u22C2','intlarhk':'\u2A17','intprod':'\u2A3C','InvisibleComma':'\u2063','InvisibleTimes':'\u2062','iocy':'\u0451','IOcy':'\u0401','iogon':'\u012F','Iogon':'\u012E','iopf':'\uD835\uDD5A','Iopf':'\uD835\uDD40','iota':'\u03B9','Iota':'\u0399','iprod':'\u2A3C','iquest':'\xBF','iscr':'\uD835\uDCBE','Iscr':'\u2110','isin':'\u2208','isindot':'\u22F5','isinE':'\u22F9','isins':'\u22F4','isinsv':'\u22F3','isinv':'\u2208','it':'\u2062','itilde':'\u0129','Itilde':'\u0128','iukcy':'\u0456','Iukcy':'\u0406','iuml':'\xEF','Iuml':'\xCF','jcirc':'\u0135','Jcirc':'\u0134','jcy':'\u0439','Jcy':'\u0419','jfr':'\uD835\uDD27','Jfr':'\uD835\uDD0D','jmath':'\u0237','jopf':'\uD835\uDD5B','Jopf':'\uD835\uDD41','jscr':'\uD835\uDCBF','Jscr':'\uD835\uDCA5','jsercy':'\u0458','Jsercy':'\u0408','jukcy':'\u0454','Jukcy':'\u0404','kappa':'\u03BA','Kappa':'\u039A','kappav':'\u03F0','kcedil':'\u0137','Kcedil':'\u0136','kcy':'\u043A','Kcy':'\u041A','kfr':'\uD835\uDD28','Kfr':'\uD835\uDD0E','kgreen':'\u0138','khcy':'\u0445','KHcy':'\u0425','kjcy':'\u045C','KJcy':'\u040C','kopf':'\uD835\uDD5C','Kopf':'\uD835\uDD42','kscr':'\uD835\uDCC0','Kscr':'\uD835\uDCA6','lAarr':'\u21DA','lacute':'\u013A','Lacute':'\u0139','laemptyv':'\u29B4','lagran':'\u2112','lambda':'\u03BB','Lambda':'\u039B','lang':'\u27E8','Lang':'\u27EA','langd':'\u2991','langle':'\u27E8','lap':'\u2A85','Laplacetrf':'\u2112','laquo':'\xAB','larr':'\u2190','lArr':'\u21D0','Larr':'\u219E','larrb':'\u21E4','larrbfs':'\u291F','larrfs':'\u291D','larrhk':'\u21A9','larrlp':'\u21AB','larrpl':'\u2939','larrsim':'\u2973','larrtl':'\u21A2','lat':'\u2AAB','latail':'\u2919','lAtail':'\u291B','late':'\u2AAD','lates':'\u2AAD\uFE00','lbarr':'\u290C','lBarr':'\u290E','lbbrk':'\u2772','lbrace':'{','lbrack':'[','lbrke':'\u298B','lbrksld':'\u298F','lbrkslu':'\u298D','lcaron':'\u013E','Lcaron':'\u013D','lcedil':'\u013C','Lcedil':'\u013B','lceil':'\u2308','lcub':'{','lcy':'\u043B','Lcy':'\u041B','ldca':'\u2936','ldquo':'\u201C','ldquor':'\u201E','ldrdhar':'\u2967','ldrushar':'\u294B','ldsh':'\u21B2','le':'\u2264','lE':'\u2266','LeftAngleBracket':'\u27E8','leftarrow':'\u2190','Leftarrow':'\u21D0','LeftArrow':'\u2190','LeftArrowBar':'\u21E4','LeftArrowRightArrow':'\u21C6','leftarrowtail':'\u21A2','LeftCeiling':'\u2308','LeftDoubleBracket':'\u27E6','LeftDownTeeVector':'\u2961','LeftDownVector':'\u21C3','LeftDownVectorBar':'\u2959','LeftFloor':'\u230A','leftharpoondown':'\u21BD','leftharpoonup':'\u21BC','leftleftarrows':'\u21C7','leftrightarrow':'\u2194','Leftrightarrow':'\u21D4','LeftRightArrow':'\u2194','leftrightarrows':'\u21C6','leftrightharpoons':'\u21CB','leftrightsquigarrow':'\u21AD','LeftRightVector':'\u294E','LeftTee':'\u22A3','LeftTeeArrow':'\u21A4','LeftTeeVector':'\u295A','leftthreetimes':'\u22CB','LeftTriangle':'\u22B2','LeftTriangleBar':'\u29CF','LeftTriangleEqual':'\u22B4','LeftUpDownVector':'\u2951','LeftUpTeeVector':'\u2960','LeftUpVector':'\u21BF','LeftUpVectorBar':'\u2958','LeftVector':'\u21BC','LeftVectorBar':'\u2952','leg':'\u22DA','lEg':'\u2A8B','leq':'\u2264','leqq':'\u2266','leqslant':'\u2A7D','les':'\u2A7D','lescc':'\u2AA8','lesdot':'\u2A7F','lesdoto':'\u2A81','lesdotor':'\u2A83','lesg':'\u22DA\uFE00','lesges':'\u2A93','lessapprox':'\u2A85','lessdot':'\u22D6','lesseqgtr':'\u22DA','lesseqqgtr':'\u2A8B','LessEqualGreater':'\u22DA','LessFullEqual':'\u2266','LessGreater':'\u2276','lessgtr':'\u2276','LessLess':'\u2AA1','lesssim':'\u2272','LessSlantEqual':'\u2A7D','LessTilde':'\u2272','lfisht':'\u297C','lfloor':'\u230A','lfr':'\uD835\uDD29','Lfr':'\uD835\uDD0F','lg':'\u2276','lgE':'\u2A91','lHar':'\u2962','lhard':'\u21BD','lharu':'\u21BC','lharul':'\u296A','lhblk':'\u2584','ljcy':'\u0459','LJcy':'\u0409','ll':'\u226A','Ll':'\u22D8','llarr':'\u21C7','llcorner':'\u231E','Lleftarrow':'\u21DA','llhard':'\u296B','lltri':'\u25FA','lmidot':'\u0140','Lmidot':'\u013F','lmoust':'\u23B0','lmoustache':'\u23B0','lnap':'\u2A89','lnapprox':'\u2A89','lne':'\u2A87','lnE':'\u2268','lneq':'\u2A87','lneqq':'\u2268','lnsim':'\u22E6','loang':'\u27EC','loarr':'\u21FD','lobrk':'\u27E6','longleftarrow':'\u27F5','Longleftarrow':'\u27F8','LongLeftArrow':'\u27F5','longleftrightarrow':'\u27F7','Longleftrightarrow':'\u27FA','LongLeftRightArrow':'\u27F7','longmapsto':'\u27FC','longrightarrow':'\u27F6','Longrightarrow':'\u27F9','LongRightArrow':'\u27F6','looparrowleft':'\u21AB','looparrowright':'\u21AC','lopar':'\u2985','lopf':'\uD835\uDD5D','Lopf':'\uD835\uDD43','loplus':'\u2A2D','lotimes':'\u2A34','lowast':'\u2217','lowbar':'_','LowerLeftArrow':'\u2199','LowerRightArrow':'\u2198','loz':'\u25CA','lozenge':'\u25CA','lozf':'\u29EB','lpar':'(','lparlt':'\u2993','lrarr':'\u21C6','lrcorner':'\u231F','lrhar':'\u21CB','lrhard':'\u296D','lrm':'\u200E','lrtri':'\u22BF','lsaquo':'\u2039','lscr':'\uD835\uDCC1','Lscr':'\u2112','lsh':'\u21B0','Lsh':'\u21B0','lsim':'\u2272','lsime':'\u2A8D','lsimg':'\u2A8F','lsqb':'[','lsquo':'\u2018','lsquor':'\u201A','lstrok':'\u0142','Lstrok':'\u0141','lt':'<','Lt':'\u226A','LT':'<','ltcc':'\u2AA6','ltcir':'\u2A79','ltdot':'\u22D6','lthree':'\u22CB','ltimes':'\u22C9','ltlarr':'\u2976','ltquest':'\u2A7B','ltri':'\u25C3','ltrie':'\u22B4','ltrif':'\u25C2','ltrPar':'\u2996','lurdshar':'\u294A','luruhar':'\u2966','lvertneqq':'\u2268\uFE00','lvnE':'\u2268\uFE00','macr':'\xAF','male':'\u2642','malt':'\u2720','maltese':'\u2720','map':'\u21A6','Map':'\u2905','mapsto':'\u21A6','mapstodown':'\u21A7','mapstoleft':'\u21A4','mapstoup':'\u21A5','marker':'\u25AE','mcomma':'\u2A29','mcy':'\u043C','Mcy':'\u041C','mdash':'\u2014','mDDot':'\u223A','measuredangle':'\u2221','MediumSpace':'\u205F','Mellintrf':'\u2133','mfr':'\uD835\uDD2A','Mfr':'\uD835\uDD10','mho':'\u2127','micro':'\xB5','mid':'\u2223','midast':'*','midcir':'\u2AF0','middot':'\xB7','minus':'\u2212','minusb':'\u229F','minusd':'\u2238','minusdu':'\u2A2A','MinusPlus':'\u2213','mlcp':'\u2ADB','mldr':'\u2026','mnplus':'\u2213','models':'\u22A7','mopf':'\uD835\uDD5E','Mopf':'\uD835\uDD44','mp':'\u2213','mscr':'\uD835\uDCC2','Mscr':'\u2133','mstpos':'\u223E','mu':'\u03BC','Mu':'\u039C','multimap':'\u22B8','mumap':'\u22B8','nabla':'\u2207','nacute':'\u0144','Nacute':'\u0143','nang':'\u2220\u20D2','nap':'\u2249','napE':'\u2A70\u0338','napid':'\u224B\u0338','napos':'\u0149','napprox':'\u2249','natur':'\u266E','natural':'\u266E','naturals':'\u2115','nbsp':'\xA0','nbump':'\u224E\u0338','nbumpe':'\u224F\u0338','ncap':'\u2A43','ncaron':'\u0148','Ncaron':'\u0147','ncedil':'\u0146','Ncedil':'\u0145','ncong':'\u2247','ncongdot':'\u2A6D\u0338','ncup':'\u2A42','ncy':'\u043D','Ncy':'\u041D','ndash':'\u2013','ne':'\u2260','nearhk':'\u2924','nearr':'\u2197','neArr':'\u21D7','nearrow':'\u2197','nedot':'\u2250\u0338','NegativeMediumSpace':'\u200B','NegativeThickSpace':'\u200B','NegativeThinSpace':'\u200B','NegativeVeryThinSpace':'\u200B','nequiv':'\u2262','nesear':'\u2928','nesim':'\u2242\u0338','NestedGreaterGreater':'\u226B','NestedLessLess':'\u226A','NewLine':'\n','nexist':'\u2204','nexists':'\u2204','nfr':'\uD835\uDD2B','Nfr':'\uD835\uDD11','nge':'\u2271','ngE':'\u2267\u0338','ngeq':'\u2271','ngeqq':'\u2267\u0338','ngeqslant':'\u2A7E\u0338','nges':'\u2A7E\u0338','nGg':'\u22D9\u0338','ngsim':'\u2275','ngt':'\u226F','nGt':'\u226B\u20D2','ngtr':'\u226F','nGtv':'\u226B\u0338','nharr':'\u21AE','nhArr':'\u21CE','nhpar':'\u2AF2','ni':'\u220B','nis':'\u22FC','nisd':'\u22FA','niv':'\u220B','njcy':'\u045A','NJcy':'\u040A','nlarr':'\u219A','nlArr':'\u21CD','nldr':'\u2025','nle':'\u2270','nlE':'\u2266\u0338','nleftarrow':'\u219A','nLeftarrow':'\u21CD','nleftrightarrow':'\u21AE','nLeftrightarrow':'\u21CE','nleq':'\u2270','nleqq':'\u2266\u0338','nleqslant':'\u2A7D\u0338','nles':'\u2A7D\u0338','nless':'\u226E','nLl':'\u22D8\u0338','nlsim':'\u2274','nlt':'\u226E','nLt':'\u226A\u20D2','nltri':'\u22EA','nltrie':'\u22EC','nLtv':'\u226A\u0338','nmid':'\u2224','NoBreak':'\u2060','NonBreakingSpace':'\xA0','nopf':'\uD835\uDD5F','Nopf':'\u2115','not':'\xAC','Not':'\u2AEC','NotCongruent':'\u2262','NotCupCap':'\u226D','NotDoubleVerticalBar':'\u2226','NotElement':'\u2209','NotEqual':'\u2260','NotEqualTilde':'\u2242\u0338','NotExists':'\u2204','NotGreater':'\u226F','NotGreaterEqual':'\u2271','NotGreaterFullEqual':'\u2267\u0338','NotGreaterGreater':'\u226B\u0338','NotGreaterLess':'\u2279','NotGreaterSlantEqual':'\u2A7E\u0338','NotGreaterTilde':'\u2275','NotHumpDownHump':'\u224E\u0338','NotHumpEqual':'\u224F\u0338','notin':'\u2209','notindot':'\u22F5\u0338','notinE':'\u22F9\u0338','notinva':'\u2209','notinvb':'\u22F7','notinvc':'\u22F6','NotLeftTriangle':'\u22EA','NotLeftTriangleBar':'\u29CF\u0338','NotLeftTriangleEqual':'\u22EC','NotLess':'\u226E','NotLessEqual':'\u2270','NotLessGreater':'\u2278','NotLessLess':'\u226A\u0338','NotLessSlantEqual':'\u2A7D\u0338','NotLessTilde':'\u2274','NotNestedGreaterGreater':'\u2AA2\u0338','NotNestedLessLess':'\u2AA1\u0338','notni':'\u220C','notniva':'\u220C','notnivb':'\u22FE','notnivc':'\u22FD','NotPrecedes':'\u2280','NotPrecedesEqual':'\u2AAF\u0338','NotPrecedesSlantEqual':'\u22E0','NotReverseElement':'\u220C','NotRightTriangle':'\u22EB','NotRightTriangleBar':'\u29D0\u0338','NotRightTriangleEqual':'\u22ED','NotSquareSubset':'\u228F\u0338','NotSquareSubsetEqual':'\u22E2','NotSquareSuperset':'\u2290\u0338','NotSquareSupersetEqual':'\u22E3','NotSubset':'\u2282\u20D2','NotSubsetEqual':'\u2288','NotSucceeds':'\u2281','NotSucceedsEqual':'\u2AB0\u0338','NotSucceedsSlantEqual':'\u22E1','NotSucceedsTilde':'\u227F\u0338','NotSuperset':'\u2283\u20D2','NotSupersetEqual':'\u2289','NotTilde':'\u2241','NotTildeEqual':'\u2244','NotTildeFullEqual':'\u2247','NotTildeTilde':'\u2249','NotVerticalBar':'\u2224','npar':'\u2226','nparallel':'\u2226','nparsl':'\u2AFD\u20E5','npart':'\u2202\u0338','npolint':'\u2A14','npr':'\u2280','nprcue':'\u22E0','npre':'\u2AAF\u0338','nprec':'\u2280','npreceq':'\u2AAF\u0338','nrarr':'\u219B','nrArr':'\u21CF','nrarrc':'\u2933\u0338','nrarrw':'\u219D\u0338','nrightarrow':'\u219B','nRightarrow':'\u21CF','nrtri':'\u22EB','nrtrie':'\u22ED','nsc':'\u2281','nsccue':'\u22E1','nsce':'\u2AB0\u0338','nscr':'\uD835\uDCC3','Nscr':'\uD835\uDCA9','nshortmid':'\u2224','nshortparallel':'\u2226','nsim':'\u2241','nsime':'\u2244','nsimeq':'\u2244','nsmid':'\u2224','nspar':'\u2226','nsqsube':'\u22E2','nsqsupe':'\u22E3','nsub':'\u2284','nsube':'\u2288','nsubE':'\u2AC5\u0338','nsubset':'\u2282\u20D2','nsubseteq':'\u2288','nsubseteqq':'\u2AC5\u0338','nsucc':'\u2281','nsucceq':'\u2AB0\u0338','nsup':'\u2285','nsupe':'\u2289','nsupE':'\u2AC6\u0338','nsupset':'\u2283\u20D2','nsupseteq':'\u2289','nsupseteqq':'\u2AC6\u0338','ntgl':'\u2279','ntilde':'\xF1','Ntilde':'\xD1','ntlg':'\u2278','ntriangleleft':'\u22EA','ntrianglelefteq':'\u22EC','ntriangleright':'\u22EB','ntrianglerighteq':'\u22ED','nu':'\u03BD','Nu':'\u039D','num':'#','numero':'\u2116','numsp':'\u2007','nvap':'\u224D\u20D2','nvdash':'\u22AC','nvDash':'\u22AD','nVdash':'\u22AE','nVDash':'\u22AF','nvge':'\u2265\u20D2','nvgt':'>\u20D2','nvHarr':'\u2904','nvinfin':'\u29DE','nvlArr':'\u2902','nvle':'\u2264\u20D2','nvlt':'<\u20D2','nvltrie':'\u22B4\u20D2','nvrArr':'\u2903','nvrtrie':'\u22B5\u20D2','nvsim':'\u223C\u20D2','nwarhk':'\u2923','nwarr':'\u2196','nwArr':'\u21D6','nwarrow':'\u2196','nwnear':'\u2927','oacute':'\xF3','Oacute':'\xD3','oast':'\u229B','ocir':'\u229A','ocirc':'\xF4','Ocirc':'\xD4','ocy':'\u043E','Ocy':'\u041E','odash':'\u229D','odblac':'\u0151','Odblac':'\u0150','odiv':'\u2A38','odot':'\u2299','odsold':'\u29BC','oelig':'\u0153','OElig':'\u0152','ofcir':'\u29BF','ofr':'\uD835\uDD2C','Ofr':'\uD835\uDD12','ogon':'\u02DB','ograve':'\xF2','Ograve':'\xD2','ogt':'\u29C1','ohbar':'\u29B5','ohm':'\u03A9','oint':'\u222E','olarr':'\u21BA','olcir':'\u29BE','olcross':'\u29BB','oline':'\u203E','olt':'\u29C0','omacr':'\u014D','Omacr':'\u014C','omega':'\u03C9','Omega':'\u03A9','omicron':'\u03BF','Omicron':'\u039F','omid':'\u29B6','ominus':'\u2296','oopf':'\uD835\uDD60','Oopf':'\uD835\uDD46','opar':'\u29B7','OpenCurlyDoubleQuote':'\u201C','OpenCurlyQuote':'\u2018','operp':'\u29B9','oplus':'\u2295','or':'\u2228','Or':'\u2A54','orarr':'\u21BB','ord':'\u2A5D','order':'\u2134','orderof':'\u2134','ordf':'\xAA','ordm':'\xBA','origof':'\u22B6','oror':'\u2A56','orslope':'\u2A57','orv':'\u2A5B','oS':'\u24C8','oscr':'\u2134','Oscr':'\uD835\uDCAA','oslash':'\xF8','Oslash':'\xD8','osol':'\u2298','otilde':'\xF5','Otilde':'\xD5','otimes':'\u2297','Otimes':'\u2A37','otimesas':'\u2A36','ouml':'\xF6','Ouml':'\xD6','ovbar':'\u233D','OverBar':'\u203E','OverBrace':'\u23DE','OverBracket':'\u23B4','OverParenthesis':'\u23DC','par':'\u2225','para':'\xB6','parallel':'\u2225','parsim':'\u2AF3','parsl':'\u2AFD','part':'\u2202','PartialD':'\u2202','pcy':'\u043F','Pcy':'\u041F','percnt':'%','period':'.','permil':'\u2030','perp':'\u22A5','pertenk':'\u2031','pfr':'\uD835\uDD2D','Pfr':'\uD835\uDD13','phi':'\u03C6','Phi':'\u03A6','phiv':'\u03D5','phmmat':'\u2133','phone':'\u260E','pi':'\u03C0','Pi':'\u03A0','pitchfork':'\u22D4','piv':'\u03D6','planck':'\u210F','planckh':'\u210E','plankv':'\u210F','plus':'+','plusacir':'\u2A23','plusb':'\u229E','pluscir':'\u2A22','plusdo':'\u2214','plusdu':'\u2A25','pluse':'\u2A72','PlusMinus':'\xB1','plusmn':'\xB1','plussim':'\u2A26','plustwo':'\u2A27','pm':'\xB1','Poincareplane':'\u210C','pointint':'\u2A15','popf':'\uD835\uDD61','Popf':'\u2119','pound':'\xA3','pr':'\u227A','Pr':'\u2ABB','prap':'\u2AB7','prcue':'\u227C','pre':'\u2AAF','prE':'\u2AB3','prec':'\u227A','precapprox':'\u2AB7','preccurlyeq':'\u227C','Precedes':'\u227A','PrecedesEqual':'\u2AAF','PrecedesSlantEqual':'\u227C','PrecedesTilde':'\u227E','preceq':'\u2AAF','precnapprox':'\u2AB9','precneqq':'\u2AB5','precnsim':'\u22E8','precsim':'\u227E','prime':'\u2032','Prime':'\u2033','primes':'\u2119','prnap':'\u2AB9','prnE':'\u2AB5','prnsim':'\u22E8','prod':'\u220F','Product':'\u220F','profalar':'\u232E','profline':'\u2312','profsurf':'\u2313','prop':'\u221D','Proportion':'\u2237','Proportional':'\u221D','propto':'\u221D','prsim':'\u227E','prurel':'\u22B0','pscr':'\uD835\uDCC5','Pscr':'\uD835\uDCAB','psi':'\u03C8','Psi':'\u03A8','puncsp':'\u2008','qfr':'\uD835\uDD2E','Qfr':'\uD835\uDD14','qint':'\u2A0C','qopf':'\uD835\uDD62','Qopf':'\u211A','qprime':'\u2057','qscr':'\uD835\uDCC6','Qscr':'\uD835\uDCAC','quaternions':'\u210D','quatint':'\u2A16','quest':'?','questeq':'\u225F','quot':'"','QUOT':'"','rAarr':'\u21DB','race':'\u223D\u0331','racute':'\u0155','Racute':'\u0154','radic':'\u221A','raemptyv':'\u29B3','rang':'\u27E9','Rang':'\u27EB','rangd':'\u2992','range':'\u29A5','rangle':'\u27E9','raquo':'\xBB','rarr':'\u2192','rArr':'\u21D2','Rarr':'\u21A0','rarrap':'\u2975','rarrb':'\u21E5','rarrbfs':'\u2920','rarrc':'\u2933','rarrfs':'\u291E','rarrhk':'\u21AA','rarrlp':'\u21AC','rarrpl':'\u2945','rarrsim':'\u2974','rarrtl':'\u21A3','Rarrtl':'\u2916','rarrw':'\u219D','ratail':'\u291A','rAtail':'\u291C','ratio':'\u2236','rationals':'\u211A','rbarr':'\u290D','rBarr':'\u290F','RBarr':'\u2910','rbbrk':'\u2773','rbrace':'}','rbrack':']','rbrke':'\u298C','rbrksld':'\u298E','rbrkslu':'\u2990','rcaron':'\u0159','Rcaron':'\u0158','rcedil':'\u0157','Rcedil':'\u0156','rceil':'\u2309','rcub':'}','rcy':'\u0440','Rcy':'\u0420','rdca':'\u2937','rdldhar':'\u2969','rdquo':'\u201D','rdquor':'\u201D','rdsh':'\u21B3','Re':'\u211C','real':'\u211C','realine':'\u211B','realpart':'\u211C','reals':'\u211D','rect':'\u25AD','reg':'\xAE','REG':'\xAE','ReverseElement':'\u220B','ReverseEquilibrium':'\u21CB','ReverseUpEquilibrium':'\u296F','rfisht':'\u297D','rfloor':'\u230B','rfr':'\uD835\uDD2F','Rfr':'\u211C','rHar':'\u2964','rhard':'\u21C1','rharu':'\u21C0','rharul':'\u296C','rho':'\u03C1','Rho':'\u03A1','rhov':'\u03F1','RightAngleBracket':'\u27E9','rightarrow':'\u2192','Rightarrow':'\u21D2','RightArrow':'\u2192','RightArrowBar':'\u21E5','RightArrowLeftArrow':'\u21C4','rightarrowtail':'\u21A3','RightCeiling':'\u2309','RightDoubleBracket':'\u27E7','RightDownTeeVector':'\u295D','RightDownVector':'\u21C2','RightDownVectorBar':'\u2955','RightFloor':'\u230B','rightharpoondown':'\u21C1','rightharpoonup':'\u21C0','rightleftarrows':'\u21C4','rightleftharpoons':'\u21CC','rightrightarrows':'\u21C9','rightsquigarrow':'\u219D','RightTee':'\u22A2','RightTeeArrow':'\u21A6','RightTeeVector':'\u295B','rightthreetimes':'\u22CC','RightTriangle':'\u22B3','RightTriangleBar':'\u29D0','RightTriangleEqual':'\u22B5','RightUpDownVector':'\u294F','RightUpTeeVector':'\u295C','RightUpVector':'\u21BE','RightUpVectorBar':'\u2954','RightVector':'\u21C0','RightVectorBar':'\u2953','ring':'\u02DA','risingdotseq':'\u2253','rlarr':'\u21C4','rlhar':'\u21CC','rlm':'\u200F','rmoust':'\u23B1','rmoustache':'\u23B1','rnmid':'\u2AEE','roang':'\u27ED','roarr':'\u21FE','robrk':'\u27E7','ropar':'\u2986','ropf':'\uD835\uDD63','Ropf':'\u211D','roplus':'\u2A2E','rotimes':'\u2A35','RoundImplies':'\u2970','rpar':')','rpargt':'\u2994','rppolint':'\u2A12','rrarr':'\u21C9','Rrightarrow':'\u21DB','rsaquo':'\u203A','rscr':'\uD835\uDCC7','Rscr':'\u211B','rsh':'\u21B1','Rsh':'\u21B1','rsqb':']','rsquo':'\u2019','rsquor':'\u2019','rthree':'\u22CC','rtimes':'\u22CA','rtri':'\u25B9','rtrie':'\u22B5','rtrif':'\u25B8','rtriltri':'\u29CE','RuleDelayed':'\u29F4','ruluhar':'\u2968','rx':'\u211E','sacute':'\u015B','Sacute':'\u015A','sbquo':'\u201A','sc':'\u227B','Sc':'\u2ABC','scap':'\u2AB8','scaron':'\u0161','Scaron':'\u0160','sccue':'\u227D','sce':'\u2AB0','scE':'\u2AB4','scedil':'\u015F','Scedil':'\u015E','scirc':'\u015D','Scirc':'\u015C','scnap':'\u2ABA','scnE':'\u2AB6','scnsim':'\u22E9','scpolint':'\u2A13','scsim':'\u227F','scy':'\u0441','Scy':'\u0421','sdot':'\u22C5','sdotb':'\u22A1','sdote':'\u2A66','searhk':'\u2925','searr':'\u2198','seArr':'\u21D8','searrow':'\u2198','sect':'\xA7','semi':';','seswar':'\u2929','setminus':'\u2216','setmn':'\u2216','sext':'\u2736','sfr':'\uD835\uDD30','Sfr':'\uD835\uDD16','sfrown':'\u2322','sharp':'\u266F','shchcy':'\u0449','SHCHcy':'\u0429','shcy':'\u0448','SHcy':'\u0428','ShortDownArrow':'\u2193','ShortLeftArrow':'\u2190','shortmid':'\u2223','shortparallel':'\u2225','ShortRightArrow':'\u2192','ShortUpArrow':'\u2191','shy':'\xAD','sigma':'\u03C3','Sigma':'\u03A3','sigmaf':'\u03C2','sigmav':'\u03C2','sim':'\u223C','simdot':'\u2A6A','sime':'\u2243','simeq':'\u2243','simg':'\u2A9E','simgE':'\u2AA0','siml':'\u2A9D','simlE':'\u2A9F','simne':'\u2246','simplus':'\u2A24','simrarr':'\u2972','slarr':'\u2190','SmallCircle':'\u2218','smallsetminus':'\u2216','smashp':'\u2A33','smeparsl':'\u29E4','smid':'\u2223','smile':'\u2323','smt':'\u2AAA','smte':'\u2AAC','smtes':'\u2AAC\uFE00','softcy':'\u044C','SOFTcy':'\u042C','sol':'/','solb':'\u29C4','solbar':'\u233F','sopf':'\uD835\uDD64','Sopf':'\uD835\uDD4A','spades':'\u2660','spadesuit':'\u2660','spar':'\u2225','sqcap':'\u2293','sqcaps':'\u2293\uFE00','sqcup':'\u2294','sqcups':'\u2294\uFE00','Sqrt':'\u221A','sqsub':'\u228F','sqsube':'\u2291','sqsubset':'\u228F','sqsubseteq':'\u2291','sqsup':'\u2290','sqsupe':'\u2292','sqsupset':'\u2290','sqsupseteq':'\u2292','squ':'\u25A1','square':'\u25A1','Square':'\u25A1','SquareIntersection':'\u2293','SquareSubset':'\u228F','SquareSubsetEqual':'\u2291','SquareSuperset':'\u2290','SquareSupersetEqual':'\u2292','SquareUnion':'\u2294','squarf':'\u25AA','squf':'\u25AA','srarr':'\u2192','sscr':'\uD835\uDCC8','Sscr':'\uD835\uDCAE','ssetmn':'\u2216','ssmile':'\u2323','sstarf':'\u22C6','star':'\u2606','Star':'\u22C6','starf':'\u2605','straightepsilon':'\u03F5','straightphi':'\u03D5','strns':'\xAF','sub':'\u2282','Sub':'\u22D0','subdot':'\u2ABD','sube':'\u2286','subE':'\u2AC5','subedot':'\u2AC3','submult':'\u2AC1','subne':'\u228A','subnE':'\u2ACB','subplus':'\u2ABF','subrarr':'\u2979','subset':'\u2282','Subset':'\u22D0','subseteq':'\u2286','subseteqq':'\u2AC5','SubsetEqual':'\u2286','subsetneq':'\u228A','subsetneqq':'\u2ACB','subsim':'\u2AC7','subsub':'\u2AD5','subsup':'\u2AD3','succ':'\u227B','succapprox':'\u2AB8','succcurlyeq':'\u227D','Succeeds':'\u227B','SucceedsEqual':'\u2AB0','SucceedsSlantEqual':'\u227D','SucceedsTilde':'\u227F','succeq':'\u2AB0','succnapprox':'\u2ABA','succneqq':'\u2AB6','succnsim':'\u22E9','succsim':'\u227F','SuchThat':'\u220B','sum':'\u2211','Sum':'\u2211','sung':'\u266A','sup':'\u2283','Sup':'\u22D1','sup1':'\xB9','sup2':'\xB2','sup3':'\xB3','supdot':'\u2ABE','supdsub':'\u2AD8','supe':'\u2287','supE':'\u2AC6','supedot':'\u2AC4','Superset':'\u2283','SupersetEqual':'\u2287','suphsol':'\u27C9','suphsub':'\u2AD7','suplarr':'\u297B','supmult':'\u2AC2','supne':'\u228B','supnE':'\u2ACC','supplus':'\u2AC0','supset':'\u2283','Supset':'\u22D1','supseteq':'\u2287','supseteqq':'\u2AC6','supsetneq':'\u228B','supsetneqq':'\u2ACC','supsim':'\u2AC8','supsub':'\u2AD4','supsup':'\u2AD6','swarhk':'\u2926','swarr':'\u2199','swArr':'\u21D9','swarrow':'\u2199','swnwar':'\u292A','szlig':'\xDF','Tab':'\t','target':'\u2316','tau':'\u03C4','Tau':'\u03A4','tbrk':'\u23B4','tcaron':'\u0165','Tcaron':'\u0164','tcedil':'\u0163','Tcedil':'\u0162','tcy':'\u0442','Tcy':'\u0422','tdot':'\u20DB','telrec':'\u2315','tfr':'\uD835\uDD31','Tfr':'\uD835\uDD17','there4':'\u2234','therefore':'\u2234','Therefore':'\u2234','theta':'\u03B8','Theta':'\u0398','thetasym':'\u03D1','thetav':'\u03D1','thickapprox':'\u2248','thicksim':'\u223C','ThickSpace':'\u205F\u200A','thinsp':'\u2009','ThinSpace':'\u2009','thkap':'\u2248','thksim':'\u223C','thorn':'\xFE','THORN':'\xDE','tilde':'\u02DC','Tilde':'\u223C','TildeEqual':'\u2243','TildeFullEqual':'\u2245','TildeTilde':'\u2248','times':'\xD7','timesb':'\u22A0','timesbar':'\u2A31','timesd':'\u2A30','tint':'\u222D','toea':'\u2928','top':'\u22A4','topbot':'\u2336','topcir':'\u2AF1','topf':'\uD835\uDD65','Topf':'\uD835\uDD4B','topfork':'\u2ADA','tosa':'\u2929','tprime':'\u2034','trade':'\u2122','TRADE':'\u2122','triangle':'\u25B5','triangledown':'\u25BF','triangleleft':'\u25C3','trianglelefteq':'\u22B4','triangleq':'\u225C','triangleright':'\u25B9','trianglerighteq':'\u22B5','tridot':'\u25EC','trie':'\u225C','triminus':'\u2A3A','TripleDot':'\u20DB','triplus':'\u2A39','trisb':'\u29CD','tritime':'\u2A3B','trpezium':'\u23E2','tscr':'\uD835\uDCC9','Tscr':'\uD835\uDCAF','tscy':'\u0446','TScy':'\u0426','tshcy':'\u045B','TSHcy':'\u040B','tstrok':'\u0167','Tstrok':'\u0166','twixt':'\u226C','twoheadleftarrow':'\u219E','twoheadrightarrow':'\u21A0','uacute':'\xFA','Uacute':'\xDA','uarr':'\u2191','uArr':'\u21D1','Uarr':'\u219F','Uarrocir':'\u2949','ubrcy':'\u045E','Ubrcy':'\u040E','ubreve':'\u016D','Ubreve':'\u016C','ucirc':'\xFB','Ucirc':'\xDB','ucy':'\u0443','Ucy':'\u0423','udarr':'\u21C5','udblac':'\u0171','Udblac':'\u0170','udhar':'\u296E','ufisht':'\u297E','ufr':'\uD835\uDD32','Ufr':'\uD835\uDD18','ugrave':'\xF9','Ugrave':'\xD9','uHar':'\u2963','uharl':'\u21BF','uharr':'\u21BE','uhblk':'\u2580','ulcorn':'\u231C','ulcorner':'\u231C','ulcrop':'\u230F','ultri':'\u25F8','umacr':'\u016B','Umacr':'\u016A','uml':'\xA8','UnderBar':'_','UnderBrace':'\u23DF','UnderBracket':'\u23B5','UnderParenthesis':'\u23DD','Union':'\u22C3','UnionPlus':'\u228E','uogon':'\u0173','Uogon':'\u0172','uopf':'\uD835\uDD66','Uopf':'\uD835\uDD4C','uparrow':'\u2191','Uparrow':'\u21D1','UpArrow':'\u2191','UpArrowBar':'\u2912','UpArrowDownArrow':'\u21C5','updownarrow':'\u2195','Updownarrow':'\u21D5','UpDownArrow':'\u2195','UpEquilibrium':'\u296E','upharpoonleft':'\u21BF','upharpoonright':'\u21BE','uplus':'\u228E','UpperLeftArrow':'\u2196','UpperRightArrow':'\u2197','upsi':'\u03C5','Upsi':'\u03D2','upsih':'\u03D2','upsilon':'\u03C5','Upsilon':'\u03A5','UpTee':'\u22A5','UpTeeArrow':'\u21A5','upuparrows':'\u21C8','urcorn':'\u231D','urcorner':'\u231D','urcrop':'\u230E','uring':'\u016F','Uring':'\u016E','urtri':'\u25F9','uscr':'\uD835\uDCCA','Uscr':'\uD835\uDCB0','utdot':'\u22F0','utilde':'\u0169','Utilde':'\u0168','utri':'\u25B5','utrif':'\u25B4','uuarr':'\u21C8','uuml':'\xFC','Uuml':'\xDC','uwangle':'\u29A7','vangrt':'\u299C','varepsilon':'\u03F5','varkappa':'\u03F0','varnothing':'\u2205','varphi':'\u03D5','varpi':'\u03D6','varpropto':'\u221D','varr':'\u2195','vArr':'\u21D5','varrho':'\u03F1','varsigma':'\u03C2','varsubsetneq':'\u228A\uFE00','varsubsetneqq':'\u2ACB\uFE00','varsupsetneq':'\u228B\uFE00','varsupsetneqq':'\u2ACC\uFE00','vartheta':'\u03D1','vartriangleleft':'\u22B2','vartriangleright':'\u22B3','vBar':'\u2AE8','Vbar':'\u2AEB','vBarv':'\u2AE9','vcy':'\u0432','Vcy':'\u0412','vdash':'\u22A2','vDash':'\u22A8','Vdash':'\u22A9','VDash':'\u22AB','Vdashl':'\u2AE6','vee':'\u2228','Vee':'\u22C1','veebar':'\u22BB','veeeq':'\u225A','vellip':'\u22EE','verbar':'|','Verbar':'\u2016','vert':'|','Vert':'\u2016','VerticalBar':'\u2223','VerticalLine':'|','VerticalSeparator':'\u2758','VerticalTilde':'\u2240','VeryThinSpace':'\u200A','vfr':'\uD835\uDD33','Vfr':'\uD835\uDD19','vltri':'\u22B2','vnsub':'\u2282\u20D2','vnsup':'\u2283\u20D2','vopf':'\uD835\uDD67','Vopf':'\uD835\uDD4D','vprop':'\u221D','vrtri':'\u22B3','vscr':'\uD835\uDCCB','Vscr':'\uD835\uDCB1','vsubne':'\u228A\uFE00','vsubnE':'\u2ACB\uFE00','vsupne':'\u228B\uFE00','vsupnE':'\u2ACC\uFE00','Vvdash':'\u22AA','vzigzag':'\u299A','wcirc':'\u0175','Wcirc':'\u0174','wedbar':'\u2A5F','wedge':'\u2227','Wedge':'\u22C0','wedgeq':'\u2259','weierp':'\u2118','wfr':'\uD835\uDD34','Wfr':'\uD835\uDD1A','wopf':'\uD835\uDD68','Wopf':'\uD835\uDD4E','wp':'\u2118','wr':'\u2240','wreath':'\u2240','wscr':'\uD835\uDCCC','Wscr':'\uD835\uDCB2','xcap':'\u22C2','xcirc':'\u25EF','xcup':'\u22C3','xdtri':'\u25BD','xfr':'\uD835\uDD35','Xfr':'\uD835\uDD1B','xharr':'\u27F7','xhArr':'\u27FA','xi':'\u03BE','Xi':'\u039E','xlarr':'\u27F5','xlArr':'\u27F8','xmap':'\u27FC','xnis':'\u22FB','xodot':'\u2A00','xopf':'\uD835\uDD69','Xopf':'\uD835\uDD4F','xoplus':'\u2A01','xotime':'\u2A02','xrarr':'\u27F6','xrArr':'\u27F9','xscr':'\uD835\uDCCD','Xscr':'\uD835\uDCB3','xsqcup':'\u2A06','xuplus':'\u2A04','xutri':'\u25B3','xvee':'\u22C1','xwedge':'\u22C0','yacute':'\xFD','Yacute':'\xDD','yacy':'\u044F','YAcy':'\u042F','ycirc':'\u0177','Ycirc':'\u0176','ycy':'\u044B','Ycy':'\u042B','yen':'\xA5','yfr':'\uD835\uDD36','Yfr':'\uD835\uDD1C','yicy':'\u0457','YIcy':'\u0407','yopf':'\uD835\uDD6A','Yopf':'\uD835\uDD50','yscr':'\uD835\uDCCE','Yscr':'\uD835\uDCB4','yucy':'\u044E','YUcy':'\u042E','yuml':'\xFF','Yuml':'\u0178','zacute':'\u017A','Zacute':'\u0179','zcaron':'\u017E','Zcaron':'\u017D','zcy':'\u0437','Zcy':'\u0417','zdot':'\u017C','Zdot':'\u017B','zeetrf':'\u2128','ZeroWidthSpace':'\u200B','zeta':'\u03B6','Zeta':'\u0396','zfr':'\uD835\uDD37','Zfr':'\u2128','zhcy':'\u0436','ZHcy':'\u0416','zigrarr':'\u21DD','zopf':'\uD835\uDD6B','Zopf':'\u2124','zscr':'\uD835\uDCCF','Zscr':'\uD835\uDCB5','zwj':'\u200D','zwnj':'\u200C'};
        var decodeMapLegacy = {'aacute':'\xE1','Aacute':'\xC1','acirc':'\xE2','Acirc':'\xC2','acute':'\xB4','aelig':'\xE6','AElig':'\xC6','agrave':'\xE0','Agrave':'\xC0','amp':'&','AMP':'&','aring':'\xE5','Aring':'\xC5','atilde':'\xE3','Atilde':'\xC3','auml':'\xE4','Auml':'\xC4','brvbar':'\xA6','ccedil':'\xE7','Ccedil':'\xC7','cedil':'\xB8','cent':'\xA2','copy':'\xA9','COPY':'\xA9','curren':'\xA4','deg':'\xB0','divide':'\xF7','eacute':'\xE9','Eacute':'\xC9','ecirc':'\xEA','Ecirc':'\xCA','egrave':'\xE8','Egrave':'\xC8','eth':'\xF0','ETH':'\xD0','euml':'\xEB','Euml':'\xCB','frac12':'\xBD','frac14':'\xBC','frac34':'\xBE','gt':'>','GT':'>','iacute':'\xED','Iacute':'\xCD','icirc':'\xEE','Icirc':'\xCE','iexcl':'\xA1','igrave':'\xEC','Igrave':'\xCC','iquest':'\xBF','iuml':'\xEF','Iuml':'\xCF','laquo':'\xAB','lt':'<','LT':'<','macr':'\xAF','micro':'\xB5','middot':'\xB7','nbsp':'\xA0','not':'\xAC','ntilde':'\xF1','Ntilde':'\xD1','oacute':'\xF3','Oacute':'\xD3','ocirc':'\xF4','Ocirc':'\xD4','ograve':'\xF2','Ograve':'\xD2','ordf':'\xAA','ordm':'\xBA','oslash':'\xF8','Oslash':'\xD8','otilde':'\xF5','Otilde':'\xD5','ouml':'\xF6','Ouml':'\xD6','para':'\xB6','plusmn':'\xB1','pound':'\xA3','quot':'"','QUOT':'"','raquo':'\xBB','reg':'\xAE','REG':'\xAE','sect':'\xA7','shy':'\xAD','sup1':'\xB9','sup2':'\xB2','sup3':'\xB3','szlig':'\xDF','thorn':'\xFE','THORN':'\xDE','times':'\xD7','uacute':'\xFA','Uacute':'\xDA','ucirc':'\xFB','Ucirc':'\xDB','ugrave':'\xF9','Ugrave':'\xD9','uml':'\xA8','uuml':'\xFC','Uuml':'\xDC','yacute':'\xFD','Yacute':'\xDD','yen':'\xA5','yuml':'\xFF'};
        var decodeMapNumeric = {'0':'\uFFFD','128':'\u20AC','130':'\u201A','131':'\u0192','132':'\u201E','133':'\u2026','134':'\u2020','135':'\u2021','136':'\u02C6','137':'\u2030','138':'\u0160','139':'\u2039','140':'\u0152','142':'\u017D','145':'\u2018','146':'\u2019','147':'\u201C','148':'\u201D','149':'\u2022','150':'\u2013','151':'\u2014','152':'\u02DC','153':'\u2122','154':'\u0161','155':'\u203A','156':'\u0153','158':'\u017E','159':'\u0178'};
        var invalidReferenceCodePoints = [1,2,3,4,5,6,7,8,11,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,64976,64977,64978,64979,64980,64981,64982,64983,64984,64985,64986,64987,64988,64989,64990,64991,64992,64993,64994,64995,64996,64997,64998,64999,65000,65001,65002,65003,65004,65005,65006,65007,65534,65535,131070,131071,196606,196607,262142,262143,327678,327679,393214,393215,458750,458751,524286,524287,589822,589823,655358,655359,720894,720895,786430,786431,851966,851967,917502,917503,983038,983039,1048574,1048575,1114110,1114111];

        /*--------------------------------------------------------------------------*/

        var stringFromCharCode = String.fromCharCode;

        var object = {};
        var hasOwnProperty = object.hasOwnProperty;
        var has = function(object, propertyName) {
            return hasOwnProperty.call(object, propertyName);
        };

        var contains = function(array, value) {
            var index = -1;
            var length = array.length;
            while (++index < length) {
                if (array[index] == value) {
                    return true;
                }
            }
            return false;
        };

        var merge = function(options, defaults) {
            if (!options) {
                return defaults;
            }
            var result = {};
            var key;
            for (key in defaults) {
                // A `hasOwnProperty` check is not needed here, since only recognized
                // option names are used anyway. Any others are ignored.
                result[key] = has(options, key) ? options[key] : defaults[key];
            }
            return result;
        };

        // Modified version of `ucs2encode`; see https://mths.be/punycode.
        var codePointToSymbol = function(codePoint, strict) {
            var output = '';
            if ((codePoint >= 0xD800 && codePoint <= 0xDFFF) || codePoint > 0x10FFFF) {
                // See issue #4:
                // “Otherwise, if the number is in the range 0xD800 to 0xDFFF or is
                // greater than 0x10FFFF, then this is a parse error. Return a U+FFFD
                // REPLACEMENT CHARACTER.”
                if (strict) {
                    parseError('character reference outside the permissible Unicode range');
                }
                return '\uFFFD';
            }
            if (has(decodeMapNumeric, codePoint)) {
                if (strict) {
                    parseError('disallowed character reference');
                }
                return decodeMapNumeric[codePoint];
            }
            if (strict && contains(invalidReferenceCodePoints, codePoint)) {
                parseError('disallowed character reference');
            }
            if (codePoint > 0xFFFF) {
                codePoint -= 0x10000;
                output += stringFromCharCode(codePoint >>> 10 & 0x3FF | 0xD800);
                codePoint = 0xDC00 | codePoint & 0x3FF;
            }
            output += stringFromCharCode(codePoint);
            return output;
        };

        var hexEscape = function(codePoint) {
            return '&#x' + codePoint.toString(16).toUpperCase() + ';';
        };

        var decEscape = function(codePoint) {
            return '&#' + codePoint + ';';
        };

        var parseError = function(message) {
            throw Error('Parse error: ' + message);
        };

        /*--------------------------------------------------------------------------*/

        var encode = function(string, options) {
            options = merge(options, encode.options);
            var strict = options.strict;
            if (strict && regexInvalidRawCodePoint.test(string)) {
                parseError('forbidden code point');
            }
            var encodeEverything = options.encodeEverything;
            var useNamedReferences = options.useNamedReferences;
            var allowUnsafeSymbols = options.allowUnsafeSymbols;
            var escapeCodePoint = options.decimal ? decEscape : hexEscape;

            var escapeBmpSymbol = function(symbol) {
                return escapeCodePoint(symbol.charCodeAt(0));
            };

            if (encodeEverything) {
                // Encode ASCII symbols.
                string = string.replace(regexAsciiWhitelist, function(symbol) {
                    // Use named references if requested & possible.
                    if (useNamedReferences && has(encodeMap, symbol)) {
                        return '&' + encodeMap[symbol] + ';';
                    }
                    return escapeBmpSymbol(symbol);
                });
                // Shorten a few escapes that represent two symbols, of which at least one
                // is within the ASCII range.
                if (useNamedReferences) {
                    string = string
                        .replace(/&gt;\u20D2/g, '&nvgt;')
                        .replace(/&lt;\u20D2/g, '&nvlt;')
                        .replace(/&#x66;&#x6A;/g, '&fjlig;');
                }
                // Encode non-ASCII symbols.
                if (useNamedReferences) {
                    // Encode non-ASCII symbols that can be replaced with a named reference.
                    string = string.replace(regexEncodeNonAscii, function(string) {
                        // Note: there is no need to check `has(encodeMap, string)` here.
                        return '&' + encodeMap[string] + ';';
                    });
                }
                // Note: any remaining non-ASCII symbols are handled outside of the `if`.
            } else if (useNamedReferences) {
                // Apply named character references.
                // Encode `<>"'&` using named character references.
                if (!allowUnsafeSymbols) {
                    string = string.replace(regexEscape, function(string) {
                        return '&' + encodeMap[string] + ';'; // no need to check `has()` here
                    });
                }
                // Shorten escapes that represent two symbols, of which at least one is
                // `<>"'&`.
                string = string
                    .replace(/&gt;\u20D2/g, '&nvgt;')
                    .replace(/&lt;\u20D2/g, '&nvlt;');
                // Encode non-ASCII symbols that can be replaced with a named reference.
                string = string.replace(regexEncodeNonAscii, function(string) {
                    // Note: there is no need to check `has(encodeMap, string)` here.
                    return '&' + encodeMap[string] + ';';
                });
            } else if (!allowUnsafeSymbols) {
                // Encode `<>"'&` using hexadecimal escapes, now that they’re not handled
                // using named character references.
                string = string.replace(regexEscape, escapeBmpSymbol);
            }
            return string
                // Encode astral symbols.
                .replace(regexAstralSymbols, function($0) {
                    // https://mathiasbynens.be/notes/javascript-encoding#surrogate-formulae
                    var high = $0.charCodeAt(0);
                    var low = $0.charCodeAt(1);
                    var codePoint = (high - 0xD800) * 0x400 + low - 0xDC00 + 0x10000;
                    return escapeCodePoint(codePoint);
                })
                // Encode any remaining BMP symbols that are not printable ASCII symbols
                // using a hexadecimal escape.
                .replace(regexBmpWhitelist, escapeBmpSymbol);
        };
        // Expose default options (so they can be overridden globally).
        encode.options = {
            'allowUnsafeSymbols': false,
            'encodeEverything': false,
            'strict': false,
            'useNamedReferences': false,
            'decimal' : false
        };

        var decode = function(html, options) {
            options = merge(options, decode.options);
            var strict = options.strict;
            if (strict && regexInvalidEntity.test(html)) {
                parseError('malformed character reference');
            }
            return html.replace(regexDecode, function($0, $1, $2, $3, $4, $5, $6, $7) {
                var codePoint;
                var semicolon;
                var decDigits;
                var hexDigits;
                var reference;
                var next;
                if ($1) {
                    // Decode decimal escapes, e.g. `&#119558;`.
                    decDigits = $1;
                    semicolon = $2;
                    if (strict && !semicolon) {
                        parseError('character reference was not terminated by a semicolon');
                    }
                    codePoint = parseInt(decDigits, 10);
                    return codePointToSymbol(codePoint, strict);
                }
                if ($3) {
                    // Decode hexadecimal escapes, e.g. `&#x1D306;`.
                    hexDigits = $3;
                    semicolon = $4;
                    if (strict && !semicolon) {
                        parseError('character reference was not terminated by a semicolon');
                    }
                    codePoint = parseInt(hexDigits, 16);
                    return codePointToSymbol(codePoint, strict);
                }
                if ($5) {
                    // Decode named character references with trailing `;`, e.g. `&copy;`.
                    reference = $5;
                    if (has(decodeMap, reference)) {
                        return decodeMap[reference];
                    } else {
                        // Ambiguous ampersand. https://mths.be/notes/ambiguous-ampersands
                        if (strict) {
                            parseError(
                                'named character reference was not terminated by a semicolon'
                            );
                        }
                        return $0;
                    }
                }
                // If we’re still here, it’s a legacy reference for sure. No need for an
                // extra `if` check.
                // Decode named character references without trailing `;`, e.g. `&amp`
                // This is only a parse error if it gets converted to `&`, or if it is
                // followed by `=` in an attribute context.
                reference = $6;
                next = $7;
                if (next && options.isAttributeValue) {
                    if (strict && next == '=') {
                        parseError('`&` did not start a character reference');
                    }
                    return $0;
                } else {
                    if (strict) {
                        parseError(
                            'named character reference was not terminated by a semicolon'
                        );
                    }
                    // Note: there is no need to check `has(decodeMapLegacy, reference)`.
                    return decodeMapLegacy[reference] + (next || '');
                }
            });
        };
        // Expose default options (so they can be overridden globally).
        decode.options = {
            'isAttributeValue': false,
            'strict': false
        };

        var escape = function(string) {
            return string.replace(regexEscape, function($0) {
                // Note: there is no need to check `has(escapeMap, $0)` here.
                return escapeMap[$0];
            });
        };

        /*--------------------------------------------------------------------------*/

        var he = {
            'version': '1.1.1',
            'encode': encode,
            'decode': decode,
            'escape': escape,
            'unescape': decode
        };

        // Some AMD build optimizers, like r.js, check for specific condition patterns
        // like the following:
        if (
            typeof undefined == 'function' &&
            typeof undefined.amd == 'object' &&
            undefined.amd
        ) {
            undefined(function() {
                return he;
            });
        }	else if (freeExports && !freeExports.nodeType) {
            if (freeModule) { // in Node.js, io.js, or RingoJS v0.8.0+
                freeModule.exports = he;
            } else { // in Narwhal or RingoJS v0.7.0-
                for (var key in he) {
                    has(he, key) && (freeExports[key] = he[key]);
                }
            }
        } else { // in Rhino or a web browser
            root.he = he;
        }

    }(commonjsGlobal));
});

var he_1 = he.decode;
var he_2 = he.escape;

/**
 * Not type-checking this file because it's mostly vendor code.
 */

/*!
 * HTML Parser By John Resig (ejohn.org)
 * Modified by Juriy "kangax" Zaytsev
 * Original code by Erik Arvidsson, Mozilla Public License
 * http://erik.eae.net/simplehtmlparser/simplehtmlparser.js
 */

// Regular Expressions for parsing tags and attributes
var singleAttrIdentifier = /([^\s"'<>/=]+)/;
var singleAttrAssign = /(?:=)/;
var singleAttrValues = [
    // attr value double quotes
    /"([^"]*)"+/.source,
    // attr value, single quotes
    /'([^']*)'+/.source,
    // attr value, no quotes
    /([^\s"'=<>`]+)/.source
];
var attribute = new RegExp(
    '^\\s*' + singleAttrIdentifier.source +
    '(?:\\s*(' + singleAttrAssign.source + ')' +
    '\\s*(?:' + singleAttrValues.join('|') + '))?'
);

// could use https://www.w3.org/TR/1999/REC-xml-names-19990114/#NT-QName
// but for Vue templates we can enforce a simple charset
var ncname = '[a-zA-Z_][\\w\\-\\.]*';
var qnameCapture = '((?:' + ncname + '\\:)?' + ncname + ')';
var startTagOpen = new RegExp('^<' + qnameCapture);
var startTagClose = /^\s*(\/?)>/;
var endTag = new RegExp('^<\\/' + qnameCapture + '[^>]*>');
var doctype = /^<!DOCTYPE [^>]+>/i;
var comment = /^<!--/;
var conditionalComment = /^<!\[/;

var IS_REGEX_CAPTURING_BROKEN = false;
'x'.replace(/x(.)?/g, function (m, g) {
    IS_REGEX_CAPTURING_BROKEN = g === '';
});

// Special Elements (can contain anything)
var isPlainTextElement = makeMap('script,style,textarea', true);
var reCache = {};

var decodingMap = {
    '&lt;': '<',
    '&gt;': '>',
    '&quot;': '"',
    '&amp;': '&',
    '&#10;': '\n'
};
var encodedAttr = /&(?:lt|gt|quot|amp);/g;
var encodedAttrWithNewLines = /&(?:lt|gt|quot|amp|#10);/g;

function decodeAttr (value, shouldDecodeNewlines) {
    var re = shouldDecodeNewlines ? encodedAttrWithNewLines : encodedAttr;
    return value.replace(re, function (match) { return decodingMap[match]; })
}

function parseHTML (html, options) {
    var stack = [];
    var expectHTML = options.expectHTML;
    var isUnaryTag$$1 = options.isUnaryTag || no;
    var canBeLeftOpenTag$$1 = options.canBeLeftOpenTag || no;
    var index = 0;
    var last, lastTag;
    while (html) {
        last = html;
        // Make sure we're not in a plaintext content element like script/style
        if (!lastTag || !isPlainTextElement(lastTag)) {
            var textEnd = html.indexOf('<');
            if (textEnd === 0) {
                // Comment:
                if (comment.test(html)) {
                    var commentEnd = html.indexOf('-->');

                    if (commentEnd >= 0) {
                        advance(commentEnd + 3);
                        continue
                    }
                }

                // http://en.wikipedia.org/wiki/Conditional_comment#Downlevel-revealed_conditional_comment
                if (conditionalComment.test(html)) {
                    var conditionalEnd = html.indexOf(']>');

                    if (conditionalEnd >= 0) {
                        advance(conditionalEnd + 2);
                        continue
                    }
                }

                // Doctype:
                var doctypeMatch = html.match(doctype);
                if (doctypeMatch) {
                    advance(doctypeMatch[0].length);
                    continue
                }

                // End tag:
                var endTagMatch = html.match(endTag);
                if (endTagMatch) {
                    var curIndex = index;
                    advance(endTagMatch[0].length);
                    parseEndTag(endTagMatch[1], curIndex, index);
                    continue
                }

                // Start tag:
                var startTagMatch = parseStartTag();
                if (startTagMatch) {
                    handleStartTag(startTagMatch);
                    continue
                }
            }

            var text = (void 0), rest$1 = (void 0), next = (void 0);
            if (textEnd >= 0) {
                rest$1 = html.slice(textEnd);
                while (
                    !endTag.test(rest$1) &&
                    !startTagOpen.test(rest$1) &&
                    !comment.test(rest$1) &&
                    !conditionalComment.test(rest$1)
                    ) {
                    // < in plain text, be forgiving and treat it as text
                    next = rest$1.indexOf('<', 1);
                    if (next < 0) { break }
                    textEnd += next;
                    rest$1 = html.slice(textEnd);
                }
                text = html.substring(0, textEnd);
                advance(textEnd);
            }

            if (textEnd < 0) {
                text = html;
                html = '';
            }

            if (options.chars && text) {
                options.chars(text);
            }
        } else {
            var stackedTag = lastTag.toLowerCase();
            var reStackedTag = reCache[stackedTag] || (reCache[stackedTag] = new RegExp('([\\s\\S]*?)(</' + stackedTag + '[^>]*>)', 'i'));
            var endTagLength = 0;
            var rest = html.replace(reStackedTag, function (all, text, endTag) {
                endTagLength = endTag.length;
                if (!isPlainTextElement(stackedTag) && stackedTag !== 'noscript') {
                    text = text
                        .replace(/<!--([\s\S]*?)-->/g, '$1')
                        .replace(/<!\[CDATA\[([\s\S]*?)]]>/g, '$1');
                }
                if (options.chars) {
                    options.chars(text);
                }
                return ''
            });
            index += html.length - rest.length;
            html = rest;
            parseEndTag(stackedTag, index - endTagLength, index);
        }

        if (html === last) {
            options.chars && options.chars(html);
            if ("production" !== 'production' && !stack.length && options.warn) {
                options.warn(("Mal-formatted tag at end of template: \"" + html + "\""));
            }
            break
        }
    }

    // Clean up any remaining tags
    parseEndTag();

    function advance (n) {
        index += n;
        html = html.substring(n);
    }

    function parseStartTag () {
        var start = html.match(startTagOpen);
        if (start) {
            var match = {
                tagName: start[1],
                attrs: [],
                start: index
            };
            advance(start[0].length);
            var end, attr;
            while (!(end = html.match(startTagClose)) && (attr = html.match(attribute))) {
                advance(attr[0].length);
                match.attrs.push(attr);
            }
            if (end) {
                match.unarySlash = end[1];
                advance(end[0].length);
                match.end = index;
                return match
            }
        }
    }

    function handleStartTag (match) {
        var tagName = match.tagName;
        var unarySlash = match.unarySlash;

        if (expectHTML) {
            if (lastTag === 'p' && isNonPhrasingTag(tagName)) {
                parseEndTag(lastTag);
            }
            if (canBeLeftOpenTag$$1(tagName) && lastTag === tagName) {
                parseEndTag(tagName);
            }
        }

        var unary = isUnaryTag$$1(tagName) || tagName === 'html' && lastTag === 'head' || !!unarySlash;

        var l = match.attrs.length;
        var attrs = new Array(l);
        for (var i = 0; i < l; i++) {
            var args = match.attrs[i];
            // hackish work around FF bug https://bugzilla.mozilla.org/show_bug.cgi?id=369778
            if (IS_REGEX_CAPTURING_BROKEN && args[0].indexOf('""') === -1) {
                if (args[3] === '') { delete args[3]; }
                if (args[4] === '') { delete args[4]; }
                if (args[5] === '') { delete args[5]; }
            }
            var value = args[3] || args[4] || args[5] || '';
            attrs[i] = {
                name: args[1],
                value: decodeAttr(
                    value,
                    options.shouldDecodeNewlines
                )
            };
        }

        if (!unary) {
            stack.push({ tag: tagName, lowerCasedTag: tagName.toLowerCase(), attrs: attrs });
            lastTag = tagName;
        }

        if (options.start) {
            options.start(tagName, attrs, unary, match.start, match.end);
        }
    }

    function parseEndTag (tagName, start, end) {
        var pos, lowerCasedTagName;
        if (start == null) { start = index; }
        if (end == null) { end = index; }

        if (tagName) {
            lowerCasedTagName = tagName.toLowerCase();
        }

        // Find the closest opened tag of the same type
        if (tagName) {
            for (pos = stack.length - 1; pos >= 0; pos--) {
                if (stack[pos].lowerCasedTag === lowerCasedTagName) {
                    break
                }
            }
        } else {
            // If no tag name is provided, clean shop
            pos = 0;
        }

        if (pos >= 0) {
            // Close all the open elements, up the stack
            for (var i = stack.length - 1; i >= pos; i--) {
                if ("production" !== 'production' &&
                    (i > pos || !tagName) &&
                    options.warn) {
                    options.warn(
                        ("tag <" + (stack[i].tag) + "> has no matching end tag.")
                    );
                }
                if (options.end) {
                    options.end(stack[i].tag, start, end);
                }
            }

            // Remove the open elements from the stack
            stack.length = pos;
            lastTag = pos && stack[pos - 1].tag;
        } else if (lowerCasedTagName === 'br') {
            if (options.start) {
                options.start(tagName, [], true, start, end);
            }
        } else if (lowerCasedTagName === 'p') {
            if (options.start) {
                options.start(tagName, [], false, start, end);
            }
            if (options.end) {
                options.end(tagName, start, end);
            }
        }
    }
}

/*  */

var defaultTagRE = /\{\{((?:.|\n)+?)\}\}/g;
var regexEscapeRE = /[-.*+?^${}()|[\]\/\\]/g;

var buildRegex = cached(function (delimiters) {
    var open = delimiters[0].replace(regexEscapeRE, '\\$&');
    var close = delimiters[1].replace(regexEscapeRE, '\\$&');
    return new RegExp(open + '((?:.|\\n)+?)' + close, 'g')
});

function parseText (
    text,
    delimiters
) {
    var tagRE = delimiters ? buildRegex(delimiters) : defaultTagRE;
    if (!tagRE.test(text)) {
        return
    }
    var tokens = [];
    var lastIndex = tagRE.lastIndex = 0;
    var match, index;
    while ((match = tagRE.exec(text))) {
        index = match.index;
        // push text token
        if (index > lastIndex) {
            tokens.push(JSON.stringify(text.slice(lastIndex, index)));
        }
        // tag token
        var exp = parseFilters(match[1].trim());
        tokens.push(("_s(" + exp + ")"));
        lastIndex = index + match[0].length;
    }
    if (lastIndex < text.length) {
        tokens.push(JSON.stringify(text.slice(lastIndex)));
    }
    return tokens.join('+')
}

/*  */

var onRE = /^@|^v-on:/;
var dirRE = /^v-|^@|^:/;
var forAliasRE = /(.*?)\s+(?:in|of)\s+(.*)/;
var forIteratorRE = /\((\{[^}]*\}|[^,]*),([^,]*)(?:,([^,]*))?\)/;

var argRE = /:(.*)$/;
var bindRE = /^:|^v-bind:/;
var modifierRE = /\.[^.]+/g;

var decodeHTMLCached = cached(he_1);

// configurable state
var warn$2;
var delimiters;
var transforms;
var preTransforms;
var postTransforms;
var platformIsPreTag;
var platformMustUseProp;
var platformGetTagNamespace;

/**
 * Convert HTML string to AST.
 */
function parse (
    template,
    options
) {
    warn$2 = options.warn || baseWarn;
    platformGetTagNamespace = options.getTagNamespace || no;
    platformMustUseProp = options.mustUseProp || no;
    platformIsPreTag = options.isPreTag || no;
    preTransforms = pluckModuleFunction(options.modules, 'preTransformNode');
    transforms = pluckModuleFunction(options.modules, 'transformNode');
    postTransforms = pluckModuleFunction(options.modules, 'postTransformNode');
    delimiters = options.delimiters;

    var stack = [];
    var preserveWhitespace = options.preserveWhitespace !== false;
    var root;
    var currentParent;
    var inVPre = false;
    var inPre = false;
    var warned = false;

    function endPre (element) {
        // check pre state
        if (element.pre) {
            inVPre = false;
        }
        if (platformIsPreTag(element.tag)) {
            inPre = false;
        }
    }

    parseHTML(template, {
        warn: warn$2,
        expectHTML: options.expectHTML,
        isUnaryTag: options.isUnaryTag,
        canBeLeftOpenTag: options.canBeLeftOpenTag,
        shouldDecodeNewlines: options.shouldDecodeNewlines,
        start: function start (tag, attrs, unary) {
            // check namespace.
            // inherit parent ns if there is one
            var ns = (currentParent && currentParent.ns) || platformGetTagNamespace(tag);

            // handle IE svg bug
            /* istanbul ignore if */
            if (isIE && ns === 'svg') {
                attrs = guardIESVGBug(attrs);
            }

            var element = {
                type: 1,
                tag: tag,
                attrsList: attrs,
                attrsMap: makeAttrsMap(attrs),
                parent: currentParent,
                children: []
            };
            if (ns) {
                element.ns = ns;
            }

            if (isForbiddenTag(element) && !isServerRendering()) {
                element.forbidden = true;
                "production" !== 'production' && warn$2(
                    'Templates should only be responsible for mapping the state to the ' +
                    'UI. Avoid placing tags with side-effects in your templates, such as ' +
                    "<" + tag + ">" + ', as they will not be parsed.'
                );
            }

            // apply pre-transforms
            for (var i = 0; i < preTransforms.length; i++) {
                preTransforms[i](element, options);
            }

            if (!inVPre) {
                processPre(element);
                if (element.pre) {
                    inVPre = true;
                }
            }
            if (platformIsPreTag(element.tag)) {
                inPre = true;
            }
            if (inVPre) {
                processRawAttrs(element);
            } else {
                processFor(element);
                processIf(element);
                processOnce(element);
                processKey(element);

                // determine whether this is a plain element after
                // removing structural attributes
                element.plain = !element.key && !attrs.length;

                processRef(element);
                processSlot(element);
                processComponent(element);
                for (var i$1 = 0; i$1 < transforms.length; i$1++) {
                    transforms[i$1](element, options);
                }
                processAttrs(element);
            }

            function checkRootConstraints (el) {

            }

            // tree management
            if (!root) {
                root = element;
                checkRootConstraints(root);
            } else if (!stack.length) {
                // allow root elements with v-if, v-else-if and v-else
                if (root.if && (element.elseif || element.else)) {
                    checkRootConstraints(element);
                    addIfCondition(root, {
                        exp: element.elseif,
                        block: element
                    });
                } else {}
            }
            if (currentParent && !element.forbidden) {
                if (element.elseif || element.else) {
                    processIfConditions(element, currentParent);
                } else if (element.slotScope) { // scoped slot
                    currentParent.plain = false;
                    var name = element.slotTarget || '"default"';(currentParent.scopedSlots || (currentParent.scopedSlots = {}))[name] = element;
                } else {
                    currentParent.children.push(element);
                    element.parent = currentParent;
                }
            }
            if (!unary) {
                currentParent = element;
                stack.push(element);
            } else {
                endPre(element);
            }
            // apply post-transforms
            for (var i$2 = 0; i$2 < postTransforms.length; i$2++) {
                postTransforms[i$2](element, options);
            }
        },

        end: function end () {
            // remove trailing whitespace
            var element = stack[stack.length - 1];
            var lastNode = element.children[element.children.length - 1];
            if (lastNode && lastNode.type === 3 && lastNode.text === ' ' && !inPre) {
                element.children.pop();
            }
            // pop stack
            stack.length -= 1;
            currentParent = stack[stack.length - 1];
            endPre(element);
        },

        chars: function chars (text) {
            if (!currentParent) {
                return
            }
            // IE textarea placeholder bug
            /* istanbul ignore if */
            if (isIE &&
                currentParent.tag === 'textarea' &&
                currentParent.attrsMap.placeholder === text) {
                return
            }
            var children = currentParent.children;
            text = inPre || text.trim()
                ? isTextTag(currentParent) ? text : decodeHTMLCached(text)
                // only preserve whitespace if its not right after a starting tag
                : preserveWhitespace && children.length ? ' ' : '';
            if (text) {
                var expression;
                if (!inVPre && text !== ' ' && (expression = parseText(text, delimiters))) {
                    children.push({
                        type: 2,
                        expression: expression,
                        text: text
                    });
                } else if (text !== ' ' || !children.length || children[children.length - 1].text !== ' ') {
                    children.push({
                        type: 3,
                        text: text
                    });
                }
            }
        }
    });
    return root
}

function processPre (el) {
    if (getAndRemoveAttr(el, 'v-pre') != null) {
        el.pre = true;
    }
}

function processRawAttrs (el) {
    var l = el.attrsList.length;
    if (l) {
        var attrs = el.attrs = new Array(l);
        for (var i = 0; i < l; i++) {
            attrs[i] = {
                name: el.attrsList[i].name,
                value: JSON.stringify(el.attrsList[i].value)
            };
        }
    } else if (!el.pre) {
        // non root node in pre blocks with no attributes
        el.plain = true;
    }
}

function processKey (el) {
    var exp = getBindingAttr(el, 'key');
    if (exp) {
        if ("production" !== 'production' && el.tag === 'template') {
            warn$2("<template> cannot be keyed. Place the key on real elements instead.");
        }
        el.key = exp;
    }
}

function processRef (el) {
    var ref = getBindingAttr(el, 'ref');
    if (ref) {
        el.ref = ref;
        el.refInFor = checkInFor(el);
    }
}

function processFor (el) {
    var exp;
    if ((exp = getAndRemoveAttr(el, 'v-for'))) {
        var inMatch = exp.match(forAliasRE);
        if (!inMatch) {
            "production" !== 'production' && warn$2(
                ("Invalid v-for expression: " + exp)
            );
            return
        }
        el.for = inMatch[2].trim();
        var alias = inMatch[1].trim();
        var iteratorMatch = alias.match(forIteratorRE);
        if (iteratorMatch) {
            el.alias = iteratorMatch[1].trim();
            el.iterator1 = iteratorMatch[2].trim();
            if (iteratorMatch[3]) {
                el.iterator2 = iteratorMatch[3].trim();
            }
        } else {
            el.alias = alias;
        }
    }
}

function processIf (el) {
    var exp = getAndRemoveAttr(el, 'v-if');
    if (exp) {
        el.if = exp;
        addIfCondition(el, {
            exp: exp,
            block: el
        });
    } else {
        if (getAndRemoveAttr(el, 'v-else') != null) {
            el.else = true;
        }
        var elseif = getAndRemoveAttr(el, 'v-else-if');
        if (elseif) {
            el.elseif = elseif;
        }
    }
}

function processIfConditions (el, parent) {
    var prev = findPrevElement(parent.children);
    if (prev && prev.if) {
        addIfCondition(prev, {
            exp: el.elseif,
            block: el
        });
    } else {}
}

function findPrevElement (children) {
    var i = children.length;
    while (i--) {
        if (children[i].type === 1) {
            return children[i]
        } else {
            if ("production" !== 'production' && children[i].text !== ' ') {
                warn$2(
                    "text \"" + (children[i].text.trim()) + "\" between v-if and v-else(-if) " +
                    "will be ignored."
                );
            }
            children.pop();
        }
    }
}

function addIfCondition (el, condition) {
    if (!el.ifConditions) {
        el.ifConditions = [];
    }
    el.ifConditions.push(condition);
}

function processOnce (el) {
    var once$$1 = getAndRemoveAttr(el, 'v-once');
    if (once$$1 != null) {
        el.once = true;
    }
}

function processSlot (el) {
    if (el.tag === 'slot') {
        el.slotName = getBindingAttr(el, 'name');
        if ("production" !== 'production' && el.key) {
            warn$2(
                "`key` does not work on <slot> because slots are abstract outlets " +
                "and can possibly expand into multiple elements. " +
                "Use the key on a wrapping element instead."
            );
        }
    } else {
        var slotTarget = getBindingAttr(el, 'slot');
        if (slotTarget) {
            el.slotTarget = slotTarget === '""' ? '"default"' : slotTarget;
        }
        if (el.tag === 'template') {
            el.slotScope = getAndRemoveAttr(el, 'scope');
        }
    }
}

function processComponent (el) {
    var binding;
    if ((binding = getBindingAttr(el, 'is'))) {
        el.component = binding;
    }
    if (getAndRemoveAttr(el, 'inline-template') != null) {
        el.inlineTemplate = true;
    }
}

function processAttrs (el) {
    var list = el.attrsList;
    var i, l, name, rawName, value, modifiers, isProp;
    for (i = 0, l = list.length; i < l; i++) {
        name = rawName = list[i].name;
        value = list[i].value;
        if (dirRE.test(name)) {
            // mark element as dynamic
            el.hasBindings = true;
            // modifiers
            modifiers = parseModifiers(name);
            if (modifiers) {
                name = name.replace(modifierRE, '');
            }
            if (bindRE.test(name)) { // v-bind
                name = name.replace(bindRE, '');
                value = parseFilters(value);
                isProp = false;
                if (modifiers) {
                    if (modifiers.prop) {
                        isProp = true;
                        name = camelize(name);
                        if (name === 'innerHtml') { name = 'innerHTML'; }
                    }
                    if (modifiers.camel) {
                        name = camelize(name);
                    }
                    if (modifiers.sync) {
                        addHandler(
                            el,
                            ("update:" + (camelize(name))),
                            genAssignmentCode(value, "$event")
                        );
                    }
                }
                if (isProp || platformMustUseProp(el.tag, el.attrsMap.type, name)) {
                    addProp(el, name, value);
                } else {
                    addAttr(el, name, value);
                }
            } else if (onRE.test(name)) { // v-on
                name = name.replace(onRE, '');
                addHandler(el, name, value, modifiers, false, warn$2);
            } else { // normal directives
                name = name.replace(dirRE, '');
                // parse arg
                var argMatch = name.match(argRE);
                var arg = argMatch && argMatch[1];
                if (arg) {
                    name = name.slice(0, -(arg.length + 1));
                }
                addDirective(el, name, rawName, value, arg, modifiers);
                if ("production" !== 'production' && name === 'model') {
                    checkForAliasModel(el, value);
                }
            }
        } else {
            // literal attribute
            addAttr(el, name, JSON.stringify(value));
        }
    }
}

function checkInFor (el) {
    var parent = el;
    while (parent) {
        if (parent.for !== undefined) {
            return true
        }
        parent = parent.parent;
    }
    return false
}

function parseModifiers (name) {
    var match = name.match(modifierRE);
    if (match) {
        var ret = {};
        match.forEach(function (m) { ret[m.slice(1)] = true; });
        return ret
    }
}

function makeAttrsMap (attrs) {
    var map = {};
    for (var i = 0, l = attrs.length; i < l; i++) {
        if (
            "production" !== 'production' &&
            map[attrs[i].name] && !isIE && !isEdge
        ) {
            warn$2('duplicate attribute: ' + attrs[i].name);
        }
        map[attrs[i].name] = attrs[i].value;
    }
    return map
}

// for script (e.g. type="x/template") or style, do not decode content
function isTextTag (el) {
    return el.tag === 'script' || el.tag === 'style'
}

function isForbiddenTag (el) {
    return (
        el.tag === 'style' ||
        (el.tag === 'script' && (
            !el.attrsMap.type ||
            el.attrsMap.type === 'text/javascript'
        ))
    )
}

var ieNSBug = /^xmlns:NS\d+/;
var ieNSPrefix = /^NS\d+:/;

/* istanbul ignore next */
function guardIESVGBug (attrs) {
    var res = [];
    for (var i = 0; i < attrs.length; i++) {
        var attr = attrs[i];
        if (!ieNSBug.test(attr.name)) {
            attr.name = attr.name.replace(ieNSPrefix, '');
            res.push(attr);
        }
    }
    return res
}

function checkForAliasModel (el, value) {
    var _el = el;
    while (_el) {
        if (_el.for && _el.alias === value) {
            warn$2(
                "<" + (el.tag) + " v-model=\"" + value + "\">: " +
                "You are binding v-model directly to a v-for iteration alias. " +
                "This will not be able to modify the v-for source array because " +
                "writing to the alias is like modifying a function local variable. " +
                "Consider using an array of objects and use v-model on an object property instead."
            );
        }
        _el = _el.parent;
    }
}

/*  */

var isStaticKey;
var isPlatformReservedTag;

var genStaticKeysCached = cached(genStaticKeys$1);

/**
 * Goal of the optimizer: walk the generated template AST tree
 * and detect sub-trees that are purely static, i.e. parts of
 * the DOM that never needs to change.
 *
 * Once we detect these sub-trees, we can:
 *
 * 1. Hoist them into constants, so that we no longer need to
 *    create fresh nodes for them on each re-render;
 * 2. Completely skip them in the patching process.
 */
function optimize (root, options) {
    if (!root) { return }
    isStaticKey = genStaticKeysCached(options.staticKeys || '');
    isPlatformReservedTag = options.isReservedTag || no;
    // first pass: mark all non-static nodes.
    markStatic$1(root);
    // second pass: mark static roots.
    markStaticRoots(root, false);
}

function genStaticKeys$1 (keys) {
    return makeMap(
        'type,tag,attrsList,attrsMap,plain,parent,children,attrs' +
        (keys ? ',' + keys : '')
    )
}

function markStatic$1 (node) {
    node.static = isStatic(node);
    if (node.type === 1) {
        // do not make component slot content static. this avoids
        // 1. components not able to mutate slot nodes
        // 2. static slot content fails for hot-reloading
        if (
            !isPlatformReservedTag(node.tag) &&
            node.tag !== 'slot' &&
            node.attrsMap['inline-template'] == null
        ) {
            return
        }
        for (var i = 0, l = node.children.length; i < l; i++) {
            var child = node.children[i];
            markStatic$1(child);
            if (!child.static) {
                node.static = false;
            }
        }
    }
}

function markStaticRoots (node, isInFor) {
    if (node.type === 1) {
        if (node.static || node.once) {
            node.staticInFor = isInFor;
        }
        // For a node to qualify as a static root, it should have children that
        // are not just static text. Otherwise the cost of hoisting out will
        // outweigh the benefits and it's better off to just always render it fresh.
        if (node.static && node.children.length && !(
            node.children.length === 1 &&
            node.children[0].type === 3
        )) {
            node.staticRoot = true;
            return
        } else {
            node.staticRoot = false;
        }
        if (node.children) {
            for (var i = 0, l = node.children.length; i < l; i++) {
                markStaticRoots(node.children[i], isInFor || !!node.for);
            }
        }
        if (node.ifConditions) {
            walkThroughConditionsBlocks(node.ifConditions, isInFor);
        }
    }
}

function walkThroughConditionsBlocks (conditionBlocks, isInFor) {
    for (var i = 1, len = conditionBlocks.length; i < len; i++) {
        markStaticRoots(conditionBlocks[i].block, isInFor);
    }
}

function isStatic (node) {
    if (node.type === 2) { // expression
        return false
    }
    if (node.type === 3) { // text
        return true
    }
    return !!(node.pre || (
        !node.hasBindings && // no dynamic bindings
        !node.if && !node.for && // not v-if or v-for or v-else
        !isBuiltInTag(node.tag) && // not a built-in
        isPlatformReservedTag(node.tag) && // not a component
        !isDirectChildOfTemplateFor(node) &&
        Object.keys(node).every(isStaticKey)
    ))
}

function isDirectChildOfTemplateFor (node) {
    while (node.parent) {
        node = node.parent;
        if (node.tag !== 'template') {
            return false
        }
        if (node.for) {
            return true
        }
    }
    return false
}

/*  */

var fnExpRE = /^\s*([\w$_]+|\([^)]*?\))\s*=>|^function\s*\(/;
var simplePathRE = /^\s*[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*|\['.*?']|\[".*?"]|\[\d+]|\[[A-Za-z_$][\w$]*])*\s*$/;

// keyCode aliases
var keyCodes = {
    esc: 27,
    tab: 9,
    enter: 13,
    space: 32,
    up: 38,
    left: 37,
    right: 39,
    down: 40,
    'delete': [8, 46]
};

// #4868: modifiers that prevent the execution of the listener
// need to explicitly return null so that we can determine whether to remove
// the listener for .once
var genGuard = function (condition) { return ("if(" + condition + ")return null;"); };

var modifierCode = {
    stop: '$event.stopPropagation();',
    prevent: '$event.preventDefault();',
    self: genGuard("$event.target !== $event.currentTarget"),
    ctrl: genGuard("!$event.ctrlKey"),
    shift: genGuard("!$event.shiftKey"),
    alt: genGuard("!$event.altKey"),
    meta: genGuard("!$event.metaKey"),
    left: genGuard("'button' in $event && $event.button !== 0"),
    middle: genGuard("'button' in $event && $event.button !== 1"),
    right: genGuard("'button' in $event && $event.button !== 2")
};

function genHandlers (
    events,
    native,
    warn
) {
    var res = native ? 'nativeOn:{' : 'on:{';
    for (var name in events) {
        var handler = events[name];
        // #5330: warn click.right, since right clicks do not actually fire click events.
        if ("production" !== 'production' &&
            name === 'click' &&
            handler && handler.modifiers && handler.modifiers.right
        ) {
            warn(
                "Use \"contextmenu\" instead of \"click.right\" since right clicks " +
                "do not actually fire \"click\" events."
            );
        }
        res += "\"" + name + "\":" + (genHandler(name, handler)) + ",";
    }
    return res.slice(0, -1) + '}'
}

function genHandler (
    name,
    handler
) {
    if (!handler) {
        return 'function(){}'
    }

    if (Array.isArray(handler)) {
        return ("[" + (handler.map(function (handler) { return genHandler(name, handler); }).join(',')) + "]")
    }

    var isMethodPath = simplePathRE.test(handler.value);
    var isFunctionExpression = fnExpRE.test(handler.value);

    if (!handler.modifiers) {
        return isMethodPath || isFunctionExpression
            ? handler.value
            : ("function($event){" + (handler.value) + "}") // inline statement
    } else {
        var code = '';
        var genModifierCode = '';
        var keys = [];
        for (var key in handler.modifiers) {
            if (modifierCode[key]) {
                genModifierCode += modifierCode[key];
                // left/right
                if (keyCodes[key]) {
                    keys.push(key);
                }
            } else {
                keys.push(key);
            }
        }
        if (keys.length) {
            code += genKeyFilter(keys);
        }
        // Make sure modifiers like prevent and stop get executed after key filtering
        if (genModifierCode) {
            code += genModifierCode;
        }
        var handlerCode = isMethodPath
            ? handler.value + '($event)'
            : isFunctionExpression
                ? ("(" + (handler.value) + ")($event)")
                : handler.value;
        return ("function($event){" + code + handlerCode + "}")
    }
}

function genKeyFilter (keys) {
    return ("if(!('button' in $event)&&" + (keys.map(genFilterCode).join('&&')) + ")return null;")
}

function genFilterCode (key) {
    var keyVal = parseInt(key, 10);
    if (keyVal) {
        return ("$event.keyCode!==" + keyVal)
    }
    var alias = keyCodes[key];
    return ("_k($event.keyCode," + (JSON.stringify(key)) + (alias ? ',' + JSON.stringify(alias) : '') + ")")
}

/*  */

function bind$1 (el, dir) {
    el.wrapData = function (code) {
        return ("_b(" + code + ",'" + (el.tag) + "'," + (dir.value) + (dir.modifiers && dir.modifiers.prop ? ',true' : '') + ")")
    };
}

/*  */

var baseDirectives = {
    bind: bind$1,
    cloak: noop$1
};

/*  */

// configurable state
var warn$3;
var transforms$1;
var dataGenFns;
var platformDirectives$1;
var isPlatformReservedTag$1;
var staticRenderFns;
var onceCount;
var currentOptions;

function generate (
    ast,
    options
) {
    // save previous staticRenderFns so generate calls can be nested
    var prevStaticRenderFns = staticRenderFns;
    var currentStaticRenderFns = staticRenderFns = [];
    var prevOnceCount = onceCount;
    onceCount = 0;
    currentOptions = options;
    warn$3 = options.warn || baseWarn;
    transforms$1 = pluckModuleFunction(options.modules, 'transformCode');
    dataGenFns = pluckModuleFunction(options.modules, 'genData');
    platformDirectives$1 = options.directives || {};
    isPlatformReservedTag$1 = options.isReservedTag || no;
    var code = ast ? genElement(ast) : '_c("div")';
    staticRenderFns = prevStaticRenderFns;
    onceCount = prevOnceCount;
    return {
        render: ("with(this){return " + code + "}"),
        staticRenderFns: currentStaticRenderFns
    }
}

function genElement (el) {
    if (el.staticRoot && !el.staticProcessed) {
        return genStatic(el)
    } else if (el.once && !el.onceProcessed) {
        return genOnce(el)
    } else if (el.for && !el.forProcessed) {
        return genFor(el)
    } else if (el.if && !el.ifProcessed) {
        return genIf(el)
    } else if (el.tag === 'template' && !el.slotTarget) {
        return genChildren(el) || 'void 0'
    } else if (el.tag === 'slot') {
        return genSlot(el)
    } else {
        // component or element
        var code;
        if (el.component) {
            code = genComponent(el.component, el);
        } else {
            var data = el.plain ? undefined : genData(el);

            var children = el.inlineTemplate ? null : genChildren(el, true);
            code = "_c('" + (el.tag) + "'" + (data ? ("," + data) : '') + (children ? ("," + children) : '') + ")";
        }
        // module transforms
        for (var i = 0; i < transforms$1.length; i++) {
            code = transforms$1[i](el, code);
        }
        return code
    }
}

// hoist static sub-trees out
function genStatic (el) {
    el.staticProcessed = true;
    staticRenderFns.push(("with(this){return " + (genElement(el)) + "}"));
    return ("_m(" + (staticRenderFns.length - 1) + (el.staticInFor ? ',true' : '') + ")")
}

// v-once
function genOnce (el) {
    el.onceProcessed = true;
    if (el.if && !el.ifProcessed) {
        return genIf(el)
    } else if (el.staticInFor) {
        var key = '';
        var parent = el.parent;
        while (parent) {
            if (parent.for) {
                key = parent.key;
                break
            }
            parent = parent.parent;
        }
        if (!key) {
            "production" !== 'production' && warn$3(
                "v-once can only be used inside v-for that is keyed. "
            );
            return genElement(el)
        }
        return ("_o(" + (genElement(el)) + "," + (onceCount++) + (key ? ("," + key) : "") + ")")
    } else {
        return genStatic(el)
    }
}

function genIf (el) {
    el.ifProcessed = true; // avoid recursion
    return genIfConditions(el.ifConditions.slice())
}

function genIfConditions (conditions) {
    if (!conditions.length) {
        return '_e()'
    }

    var condition = conditions.shift();
    if (condition.exp) {
        return ("(" + (condition.exp) + ")?" + (genTernaryExp(condition.block)) + ":" + (genIfConditions(conditions)))
    } else {
        return ("" + (genTernaryExp(condition.block)))
    }

    // v-if with v-once should generate code like (a)?_m(0):_m(1)
    function genTernaryExp (el) {
        return el.once ? genOnce(el) : genElement(el)
    }
}

function genFor (el) {
    var exp = el.for;
    var alias = el.alias;
    var iterator1 = el.iterator1 ? ("," + (el.iterator1)) : '';
    var iterator2 = el.iterator2 ? ("," + (el.iterator2)) : '';

    if (
        "production" !== 'production' &&
        maybeComponent(el) && el.tag !== 'slot' && el.tag !== 'template' && !el.key
    ) {
        warn$3(
            "<" + (el.tag) + " v-for=\"" + alias + " in " + exp + "\">: component lists rendered with " +
            "v-for should have explicit keys. " +
            "See https://vuejs.org/guide/list.html#key for more info.",
            true /* tip */
        );
    }

    el.forProcessed = true; // avoid recursion
    return "_l((" + exp + ")," +
        "function(" + alias + iterator1 + iterator2 + "){" +
        "return " + (genElement(el)) +
        '})'
}

function genData (el) {
    var data = '{';

    // directives first.
    // directives may mutate the el's other properties before they are generated.
    var dirs = genDirectives(el);
    if (dirs) { data += dirs + ','; }

    // key
    if (el.key) {
        data += "key:" + (el.key) + ",";
    }
    // ref
    if (el.ref) {
        data += "ref:" + (el.ref) + ",";
    }
    if (el.refInFor) {
        data += "refInFor:true,";
    }
    // pre
    if (el.pre) {
        data += "pre:true,";
    }
    // record original tag name for components using "is" attribute
    if (el.component) {
        data += "tag:\"" + (el.tag) + "\",";
    }
    // module data generation functions
    for (var i = 0; i < dataGenFns.length; i++) {
        data += dataGenFns[i](el);
    }
    // attributes
    if (el.attrs) {
        data += "attrs:{" + (genProps(el.attrs)) + "},";
    }
    // DOM props
    if (el.props) {
        data += "domProps:{" + (genProps(el.props)) + "},";
    }
    // event handlers
    if (el.events) {
        data += (genHandlers(el.events, false, warn$3)) + ",";
    }
    if (el.nativeEvents) {
        data += (genHandlers(el.nativeEvents, true, warn$3)) + ",";
    }
    // slot target
    if (el.slotTarget) {
        data += "slot:" + (el.slotTarget) + ",";
    }
    // scoped slots
    if (el.scopedSlots) {
        data += (genScopedSlots(el.scopedSlots)) + ",";
    }
    // component v-model
    if (el.model) {
        data += "model:{value:" + (el.model.value) + ",callback:" + (el.model.callback) + ",expression:" + (el.model.expression) + "},";
    }
    // inline-template
    if (el.inlineTemplate) {
        var inlineTemplate = genInlineTemplate(el);
        if (inlineTemplate) {
            data += inlineTemplate + ",";
        }
    }
    data = data.replace(/,$/, '') + '}';
    // v-bind data wrap
    if (el.wrapData) {
        data = el.wrapData(data);
    }
    return data
}

function genDirectives (el) {
    var dirs = el.directives;
    if (!dirs) { return }
    var res = 'directives:[';
    var hasRuntime = false;
    var i, l, dir, needRuntime;
    for (i = 0, l = dirs.length; i < l; i++) {
        dir = dirs[i];
        needRuntime = true;
        var gen = platformDirectives$1[dir.name] || baseDirectives[dir.name];
        if (gen) {
            // compile-time directive that manipulates AST.
            // returns true if it also needs a runtime counterpart.
            needRuntime = !!gen(el, dir, warn$3);
        }
        if (needRuntime) {
            hasRuntime = true;
            res += "{name:\"" + (dir.name) + "\",rawName:\"" + (dir.rawName) + "\"" + (dir.value ? (",value:(" + (dir.value) + "),expression:" + (JSON.stringify(dir.value))) : '') + (dir.arg ? (",arg:\"" + (dir.arg) + "\"") : '') + (dir.modifiers ? (",modifiers:" + (JSON.stringify(dir.modifiers))) : '') + "},";
        }
    }
    if (hasRuntime) {
        return res.slice(0, -1) + ']'
    }
}

function genInlineTemplate (el) {
    var ast = el.children[0];
    if ("production" !== 'production' && (
        el.children.length > 1 || ast.type !== 1
    )) {
        warn$3('Inline-template components must have exactly one child element.');
    }
    if (ast.type === 1) {
        var inlineRenderFns = generate(ast, currentOptions);
        return ("inlineTemplate:{render:function(){" + (inlineRenderFns.render) + "},staticRenderFns:[" + (inlineRenderFns.staticRenderFns.map(function (code) { return ("function(){" + code + "}"); }).join(',')) + "]}")
    }
}

function genScopedSlots (slots) {
    return ("scopedSlots:_u([" + (Object.keys(slots).map(function (key) { return genScopedSlot(key, slots[key]); }).join(',')) + "])")
}

function genScopedSlot (key, el) {
    return "[" + key + ",function(" + (String(el.attrsMap.scope)) + "){" +
        "return " + (el.tag === 'template'
            ? genChildren(el) || 'void 0'
            : genElement(el)) + "}]"
}

function genChildren (el, checkSkip) {
    var children = el.children;
    if (children.length) {
        var el$1 = children[0];
        // optimize single v-for
        if (children.length === 1 &&
            el$1.for &&
            el$1.tag !== 'template' &&
            el$1.tag !== 'slot') {
            return genElement(el$1)
        }
        var normalizationType = checkSkip ? getNormalizationType(children) : 0;
        return ("[" + (children.map(genNode).join(',')) + "]" + (normalizationType ? ("," + normalizationType) : ''))
    }
}

// determine the normalization needed for the children array.
// 0: no normalization needed
// 1: simple normalization needed (possible 1-level deep nested array)
// 2: full normalization needed
function getNormalizationType (children) {
    var res = 0;
    for (var i = 0; i < children.length; i++) {
        var el = children[i];
        if (el.type !== 1) {
            continue
        }
        if (needsNormalization(el) ||
            (el.ifConditions && el.ifConditions.some(function (c) { return needsNormalization(c.block); }))) {
            res = 2;
            break
        }
        if (maybeComponent(el) ||
            (el.ifConditions && el.ifConditions.some(function (c) { return maybeComponent(c.block); }))) {
            res = 1;
        }
    }
    return res
}

function needsNormalization (el) {
    return el.for !== undefined || el.tag === 'template' || el.tag === 'slot'
}

function maybeComponent (el) {
    return !isPlatformReservedTag$1(el.tag)
}

function genNode (node) {
    if (node.type === 1) {
        return genElement(node)
    } else {
        return genText(node)
    }
}

function genText (text) {
    return ("_v(" + (text.type === 2
        ? text.expression // no need for () because already wrapped in _s()
        : transformSpecialNewlines(JSON.stringify(text.text))) + ")")
}

function genSlot (el) {
    var slotName = el.slotName || '"default"';
    var children = genChildren(el);
    var res = "_t(" + slotName + (children ? ("," + children) : '');
    var attrs = el.attrs && ("{" + (el.attrs.map(function (a) { return ((camelize(a.name)) + ":" + (a.value)); }).join(',')) + "}");
    var bind$$1 = el.attrsMap['v-bind'];
    if ((attrs || bind$$1) && !children) {
        res += ",null";
    }
    if (attrs) {
        res += "," + attrs;
    }
    if (bind$$1) {
        res += (attrs ? '' : ',null') + "," + bind$$1;
    }
    return res + ')'
}

// componentName is el.component, take it as argument to shun flow's pessimistic refinement
function genComponent (componentName, el) {
    var children = el.inlineTemplate ? null : genChildren(el, true);
    return ("_c(" + componentName + "," + (genData(el)) + (children ? ("," + children) : '') + ")")
}

function genProps (props) {
    var res = '';
    for (var i = 0; i < props.length; i++) {
        var prop = props[i];
        res += "\"" + (prop.name) + "\":" + (transformSpecialNewlines(prop.value)) + ",";
    }
    return res.slice(0, -1)
}

// #3895, #4268
function transformSpecialNewlines (text) {
    return text
        .replace(/\u2028/g, '\\u2028')
        .replace(/\u2029/g, '\\u2029')
}

/*  */

// detect problematic expressions in a template

/*  */

function baseCompile (
    template,
    options
) {
    var ast = parse(template.trim(), options);
    optimize(ast, options);
    var code = generate(ast, options);
    return {
        ast: ast,
        render: code.render,
        staticRenderFns: code.staticRenderFns
    }
}

function makeFunction (code, errors) {
    try {
        return new Function(code)
    } catch (err) {
        errors.push({ err: err, code: code });
        return noop$1
    }
}

function createCompiler (baseOptions) {
    var functionCompileCache = Object.create(null);

    function compile (
        template,
        options
    ) {
        var finalOptions = Object.create(baseOptions);
        var errors = [];
        var tips = [];
        finalOptions.warn = function (msg, tip$$1) {
            (tip$$1 ? tips : errors).push(msg);
        };

        if (options) {
            // merge custom modules
            if (options.modules) {
                finalOptions.modules = (baseOptions.modules || []).concat(options.modules);
            }
            // merge custom directives
            if (options.directives) {
                finalOptions.directives = extend(
                    Object.create(baseOptions.directives),
                    options.directives
                );
            }
            // copy other options
            for (var key in options) {
                if (key !== 'modules' && key !== 'directives') {
                    finalOptions[key] = options[key];
                }
            }
        }

        var compiled = baseCompile(template, finalOptions);
        compiled.errors = errors;
        compiled.tips = tips;
        return compiled
    }

    function compileToFunctions (
        template,
        options,
        vm
    ) {
        options = options || {};

        /* istanbul ignore if */
        var key = options.delimiters
            ? String(options.delimiters) + template
            : template;
        if (functionCompileCache[key]) {
            return functionCompileCache[key]
        }

        // compile
        var compiled = compile(template, options);

        // check compilation errors/tips
        var res = {};
        var fnGenErrors = [];
        res.render = makeFunction(compiled.render, fnGenErrors);
        var l = compiled.staticRenderFns.length;
        res.staticRenderFns = new Array(l);
        for (var i = 0; i < l; i++) {
            res.staticRenderFns[i] = makeFunction(compiled.staticRenderFns[i], fnGenErrors);
        }

        // check function generation errors.
        // this should only happen if there is a bug in the compiler itself.
        // mostly for codegen development use
        /* istanbul ignore if */
        return (functionCompileCache[key] = res)
    }

    return {
        compile: compile,
        compileToFunctions: compileToFunctions
    }
}

/*  */

function transformNode (el, options) {
    var warn = options.warn || baseWarn;
    var staticClass = getAndRemoveAttr(el, 'class');
    if ("production" !== 'production' && staticClass) {
        var expression = parseText(staticClass, options.delimiters);
        if (expression) {
            warn(
                "class=\"" + staticClass + "\": " +
                'Interpolation inside attributes has been removed. ' +
                'Use v-bind or the colon shorthand instead. For example, ' +
                'instead of <div class="{{ val }}">, use <div :class="val">.'
            );
        }
    }
    if (staticClass) {
        el.staticClass = JSON.stringify(staticClass);
    }
    var classBinding = getBindingAttr(el, 'class', false /* getStatic */);
    if (classBinding) {
        el.classBinding = classBinding;
    }
}

function genData$1 (el) {
    var data = '';
    if (el.staticClass) {
        data += "staticClass:" + (el.staticClass) + ",";
    }
    if (el.classBinding) {
        data += "class:" + (el.classBinding) + ",";
    }
    return data
}

var klass$1 = {
    staticKeys: ['staticClass'],
    transformNode: transformNode,
    genData: genData$1
};

/*  */

function transformNode$1 (el, options) {
    var warn = options.warn || baseWarn;
    var staticStyle = getAndRemoveAttr(el, 'style');
    if (staticStyle) {
        /* istanbul ignore if */
        el.staticStyle = JSON.stringify(parseStyleText(staticStyle));
    }

    var styleBinding = getBindingAttr(el, 'style', false /* getStatic */);
    if (styleBinding) {
        el.styleBinding = styleBinding;
    }
}

function genData$2 (el) {
    var data = '';
    if (el.staticStyle) {
        data += "staticStyle:" + (el.staticStyle) + ",";
    }
    if (el.styleBinding) {
        data += "style:(" + (el.styleBinding) + "),";
    }
    return data
}

var style$1 = {
    staticKeys: ['staticStyle'],
    transformNode: transformNode$1,
    genData: genData$2
};

var modules$1 = [
    klass$1,
    style$1
];

/*  */

function text (el, dir) {
    if (dir.value) {
        addProp(el, 'textContent', ("_s(" + (dir.value) + ")"));
    }
}

/*  */

function html (el, dir) {
    if (dir.value) {
        addProp(el, 'innerHTML', ("_s(" + (dir.value) + ")"));
    }
}

var directives$1 = {
    model: model,
    text: text,
    html: html
};

/*  */

var baseOptions = {
    expectHTML: true,
    modules: modules$1,
    directives: directives$1,
    isPreTag: isPreTag,
    isUnaryTag: isUnaryTag,
    mustUseProp: mustUseProp,
    canBeLeftOpenTag: canBeLeftOpenTag,
    isReservedTag: isReservedTag,
    getTagNamespace: getTagNamespace,
    staticKeys: genStaticKeys(modules$1)
};

var ref$1 = createCompiler(baseOptions);
var compileToFunctions = ref$1.compileToFunctions;

/*  */

var idToTemplate = cached(function (id) {
    var el = query(id);
    return el && el.html()
});

var mount = Vue$3.prototype.$mount;
Vue$3.prototype.$mount = function (
    el,
    hydrating
) {
    el = el && query(el);

    /* istanbul ignore if */
    if (el && (el === document.body || el === document.documentElement) ) {
        "production" !== 'production' && warn(
            "Do not mount Vue to <html> or <body> - mount to normal elements instead."
        );
        return this
    }

    var options = this.$options;
    // resolve template/el and convert to render function
    if (!options.render) {
        var template = options.template;
        if (template) {
            if (typeof template === 'string') {
                if (template.charAt(0) === '#') {
                    template = idToTemplate(template);
                    /* istanbul ignore if */
                    if ("production" !== 'production' && !template) {
                        warn(
                            ("Template element not found or is empty: " + (options.template)),
                            this
                        );
                    }
                }
            } else if (template.nodeType) {
                template = template.innerHTML;
            } else {
                return this
            }
        } else if (el) {
            template = getOuterHTML(el);
        }
        if (template) {
            /* istanbul ignore if */
            if ("production" !== 'production' && config$1.performance && mark) {
                mark('compile');
            }

            var ref = compileToFunctions(template, {
                shouldDecodeNewlines: shouldDecodeNewlines,
                delimiters: options.delimiters
            }, this);
            var render = ref.render;
            var staticRenderFns = ref.staticRenderFns;
            options.render = render;
            options.staticRenderFns = staticRenderFns;

            /* istanbul ignore if */
            if ("production" !== 'production' && config$1.performance && mark) {
                mark('compile end');
                measure(((this._name) + " compile"), 'compile', 'compile end');
            }
        }
    }
    return mount.call(this, el, hydrating)
};

/**
 * Get outerHTML of elements, taking care
 * of SVG elements in IE as well.
 */
function getOuterHTML (el) {
    logger.info("el:"+el.getClass());
    if (el.outerHtml()) {
        return el.outerHtml();
    } else {
        var container = document.createElement('div');
        container.appendChild(el.cloneNode(true));
        return container.innerHTML
    }
}

Vue$3.compile = compileToFunctions;

var domain;

// This constructor is used to store event handlers. Instantiating this is
// faster than explicitly calling `Object.create(null)` to get a "clean" empty
// object (tested with v8 v4.9).
function EventHandlers() {}
EventHandlers.prototype = Object.create(null);

function EventEmitter() {
    EventEmitter.init.call(this);
}
EventEmitter.usingDomains = false;

EventEmitter.prototype.domain = undefined;
EventEmitter.prototype._events = undefined;
EventEmitter.prototype._maxListeners = undefined;

// By default EventEmitters will print a warning if more than 10 listeners are
// added to it. This is a useful default which helps finding memory leaks.
EventEmitter.defaultMaxListeners = 10;

EventEmitter.init = function() {
    this.domain = null;
    if (EventEmitter.usingDomains) {
        // if there is an active domain, then attach to it.
        if (domain.active && !(this instanceof domain.Domain)) {
            this.domain = domain.active;
        }
    }

    if (!this._events || this._events === Object.getPrototypeOf(this)._events) {
        this._events = new EventHandlers();
        this._eventsCount = 0;
    }

    this._maxListeners = this._maxListeners || undefined;
};

// Obviously not all Emitters should be limited to 10. This function allows
// that to be increased. Set to zero for unlimited.
EventEmitter.prototype.setMaxListeners = function setMaxListeners(n) {
    if (typeof n !== 'number' || n < 0 || isNaN(n))
    { throw new TypeError('"n" argument must be a positive number'); }
    this._maxListeners = n;
    return this;
};

function $getMaxListeners(that) {
    if (that._maxListeners === undefined)
    { return EventEmitter.defaultMaxListeners; }
    return that._maxListeners;
}

EventEmitter.prototype.getMaxListeners = function getMaxListeners() {
    return $getMaxListeners(this);
};

// These standalone emit* functions are used to optimize calling of event
// handlers for fast cases because emit() itself often has a variable number of
// arguments and can be deoptimized because of that. These functions always have
// the same number of arguments and thus do not get deoptimized, so the code
// inside them can execute faster.
function emitNone(handler, isFn, self) {
    if (isFn)
    { handler.call(self); }
    else {
        var len = handler.length;
        var listeners = arrayClone(handler, len);
        for (var i = 0; i < len; ++i)
        { listeners[i].call(self); }
    }
}
function emitOne(handler, isFn, self, arg1) {
    if (isFn)
    { handler.call(self, arg1); }
    else {
        var len = handler.length;
        var listeners = arrayClone(handler, len);
        for (var i = 0; i < len; ++i)
        { listeners[i].call(self, arg1); }
    }
}
function emitTwo(handler, isFn, self, arg1, arg2) {
    if (isFn)
    { handler.call(self, arg1, arg2); }
    else {
        var len = handler.length;
        var listeners = arrayClone(handler, len);
        for (var i = 0; i < len; ++i)
        { listeners[i].call(self, arg1, arg2); }
    }
}
function emitThree(handler, isFn, self, arg1, arg2, arg3) {
    if (isFn)
    { handler.call(self, arg1, arg2, arg3); }
    else {
        var len = handler.length;
        var listeners = arrayClone(handler, len);
        for (var i = 0; i < len; ++i)
        { listeners[i].call(self, arg1, arg2, arg3); }
    }
}

function emitMany(handler, isFn, self, args) {
    if (isFn)
    { handler.apply(self, args); }
    else {
        var len = handler.length;
        var listeners = arrayClone(handler, len);
        for (var i = 0; i < len; ++i)
        { listeners[i].apply(self, args); }
    }
}

EventEmitter.prototype.emit = function emit(type) {
    var arguments$1 = arguments;

    var er, handler, len, args, i, events, domain;
    var needDomainExit = false;
    var doError = (type === 'error');

    events = this._events;
    if (events)
    { doError = (doError && events.error == null); }
    else if (!doError)
    { return false; }

    domain = this.domain;

    // If there is no 'error' event listener then throw.
    if (doError) {
        er = arguments[1];
        if (domain) {
            if (!er)
            { er = new Error('Uncaught, unspecified "error" event'); }
            er.domainEmitter = this;
            er.domain = domain;
            er.domainThrown = false;
            domain.emit('error', er);
        } else if (er instanceof Error) {
            throw er; // Unhandled 'error' event
        } else {
            // At least give some kind of context to the user
            var err = new Error('Uncaught, unspecified "error" event. (' + er + ')');
            err.context = er;
            throw err;
        }
        return false;
    }

    handler = events[type];

    if (!handler)
    { return false; }

    var isFn = typeof handler === 'function';
    len = arguments.length;
    switch (len) {
        // fast cases
        case 1:
            emitNone(handler, isFn, this);
            break;
        case 2:
            emitOne(handler, isFn, this, arguments[1]);
            break;
        case 3:
            emitTwo(handler, isFn, this, arguments[1], arguments[2]);
            break;
        case 4:
            emitThree(handler, isFn, this, arguments[1], arguments[2], arguments[3]);
            break;
        // slower
        default:
            args = new Array(len - 1);
            for (i = 1; i < len; i++)
            { args[i - 1] = arguments$1[i]; }
            emitMany(handler, isFn, this, args);
    }

    if (needDomainExit)
    { domain.exit(); }

    return true;
};

function _addListener(target, type, listener, prepend) {
    var m;
    var events;
    var existing;

    if (typeof listener !== 'function')
    { throw new TypeError('"listener" argument must be a function'); }

    events = target._events;
    if (!events) {
        events = target._events = new EventHandlers();
        target._eventsCount = 0;
    } else {
        // To avoid recursion in the case that type === "newListener"! Before
        // adding it to the listeners, first emit "newListener".
        if (events.newListener) {
            target.emit('newListener', type,
                listener.listener ? listener.listener : listener);

            // Re-assign `events` because a newListener handler could have caused the
            // this._events to be assigned to a new object
            events = target._events;
        }
        existing = events[type];
    }

    if (!existing) {
        // Optimize the case of one listener. Don't need the extra array object.
        existing = events[type] = listener;
        ++target._eventsCount;
    } else {
        if (typeof existing === 'function') {
            // Adding the second element, need to change to array.
            existing = events[type] = prepend ? [listener, existing] :
                [existing, listener];
        } else {
            // If we've already got an array, just append.
            if (prepend) {
                existing.unshift(listener);
            } else {
                existing.push(listener);
            }
        }

        // Check for listener leak
        if (!existing.warned) {
            m = $getMaxListeners(target);
            if (m && m > 0 && existing.length > m) {
                existing.warned = true;
                var w = new Error('Possible EventEmitter memory leak detected. ' +
                    existing.length + ' ' + type + ' listeners added. ' +
                    'Use emitter.setMaxListeners() to increase limit');
                w.name = 'MaxListenersExceededWarning';
                w.emitter = target;
                w.type = type;
                w.count = existing.length;
                emitWarning(w);
            }
        }
    }

    return target;
}
function emitWarning(e) {
    typeof console.warn === 'function' ? console.warn(e) : console.log(e);
}
EventEmitter.prototype.addListener = function addListener(type, listener) {
    return _addListener(this, type, listener, false);
};

EventEmitter.prototype.on = EventEmitter.prototype.addListener;

EventEmitter.prototype.prependListener =
    function prependListener(type, listener) {
        return _addListener(this, type, listener, true);
    };

function _onceWrap(target, type, listener) {
    var fired = false;
    function g() {
        target.removeListener(type, g);
        if (!fired) {
            fired = true;
            listener.apply(target, arguments);
        }
    }
    g.listener = listener;
    return g;
}

EventEmitter.prototype.once = function once(type, listener) {
    if (typeof listener !== 'function')
    { throw new TypeError('"listener" argument must be a function'); }
    this.on(type, _onceWrap(this, type, listener));
    return this;
};

EventEmitter.prototype.prependOnceListener =
    function prependOnceListener(type, listener) {
        if (typeof listener !== 'function')
        { throw new TypeError('"listener" argument must be a function'); }
        this.prependListener(type, _onceWrap(this, type, listener));
        return this;
    };

// emits a 'removeListener' event iff the listener was removed
EventEmitter.prototype.removeListener =
    function removeListener(type, listener) {
        var list, events, position, i, originalListener;

        if (typeof listener !== 'function')
        { throw new TypeError('"listener" argument must be a function'); }

        events = this._events;
        if (!events)
        { return this; }

        list = events[type];
        if (!list)
        { return this; }

        if (list === listener || (list.listener && list.listener === listener)) {
            if (--this._eventsCount === 0)
            { this._events = new EventHandlers(); }
            else {
                delete events[type];
                if (events.removeListener)
                { this.emit('removeListener', type, list.listener || listener); }
            }
        } else if (typeof list !== 'function') {
            position = -1;

            for (i = list.length; i-- > 0;) {
                if (list[i] === listener ||
                    (list[i].listener && list[i].listener === listener)) {
                    originalListener = list[i].listener;
                    position = i;
                    break;
                }
            }

            if (position < 0)
            { return this; }

            if (list.length === 1) {
                list[0] = undefined;
                if (--this._eventsCount === 0) {
                    this._events = new EventHandlers();
                    return this;
                } else {
                    delete events[type];
                }
            } else {
                spliceOne(list, position);
            }

            if (events.removeListener)
            { this.emit('removeListener', type, originalListener || listener); }
        }

        return this;
    };

EventEmitter.prototype.removeAllListeners =
    function removeAllListeners(type) {
        var this$1 = this;

        var listeners, events;

        events = this._events;
        if (!events)
        { return this; }

        // not listening for removeListener, no need to emit
        if (!events.removeListener) {
            if (arguments.length === 0) {
                this._events = new EventHandlers();
                this._eventsCount = 0;
            } else if (events[type]) {
                if (--this._eventsCount === 0)
                { this._events = new EventHandlers(); }
                else
                { delete events[type]; }
            }
            return this;
        }

        // emit removeListener for all listeners on all events
        if (arguments.length === 0) {
            var keys = Object.keys(events);
            for (var i = 0, key; i < keys.length; ++i) {
                key = keys[i];
                if (key === 'removeListener') { continue; }
                this$1.removeAllListeners(key);
            }
            this.removeAllListeners('removeListener');
            this._events = new EventHandlers();
            this._eventsCount = 0;
            return this;
        }

        listeners = events[type];

        if (typeof listeners === 'function') {
            this.removeListener(type, listeners);
        } else if (listeners) {
            // LIFO order
            do {
                this$1.removeListener(type, listeners[listeners.length - 1]);
            } while (listeners[0]);
        }

        return this;
    };

EventEmitter.prototype.listeners = function listeners(type) {
    var evlistener;
    var ret;
    var events = this._events;

    if (!events)
    { ret = []; }
    else {
        evlistener = events[type];
        if (!evlistener)
        { ret = []; }
        else if (typeof evlistener === 'function')
        { ret = [evlistener.listener || evlistener]; }
        else
        { ret = unwrapListeners(evlistener); }
    }

    return ret;
};

EventEmitter.listenerCount = function(emitter, type) {
    if (typeof emitter.listenerCount === 'function') {
        return emitter.listenerCount(type);
    } else {
        return listenerCount.call(emitter, type);
    }
};

EventEmitter.prototype.listenerCount = listenerCount;
function listenerCount(type) {
    var events = this._events;

    if (events) {
        var evlistener = events[type];

        if (typeof evlistener === 'function') {
            return 1;
        } else if (evlistener) {
            return evlistener.length;
        }
    }

    return 0;
}

EventEmitter.prototype.eventNames = function eventNames() {
    return this._eventsCount > 0 ? Reflect.ownKeys(this._events) : [];
};

// About 1.5x faster than the two-arg version of Array#splice().
function spliceOne(list, index) {
    for (var i = index, k = i + 1, n = list.length; k < n; i += 1, k += 1)
    { list[i] = list[k]; }
    list.pop();
}

function arrayClone(arr, i) {
    var copy = new Array(i);
    while (i--)
    { copy[i] = arr[i]; }
    return copy;
}

function unwrapListeners(arr) {
    var ret = new Array(arr.length);
    for (var i = 0; i < ret.length; ++i) {
        ret[i] = arr[i].listener || arr[i];
    }
    return ret;
}

var inherits;
if (typeof Object.create === 'function'){
    inherits = function inherits(ctor, superCtor) {
        // implementation from standard node.js 'util' module
        ctor.super_ = superCtor;
        ctor.prototype = Object.create(superCtor.prototype, {
            constructor: {
                value: ctor,
                enumerable: false,
                writable: true,
                configurable: true
            }
        });
    };
} else {
    inherits = function inherits(ctor, superCtor) {
        ctor.super_ = superCtor;
        var TempCtor = function () {};
        TempCtor.prototype = superCtor.prototype;
        ctor.prototype = new TempCtor();
        ctor.prototype.constructor = ctor;
    };
}
var inherits$1 = inherits;

// Copyright Joyent, Inc. and other Node contributors.
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the
// following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
// USE OR OTHER DEALINGS IN THE SOFTWARE.
var formatRegExp = /%[sdj%]/g;
function format(f) {
    var arguments$1 = arguments;

    if (!isString(f)) {
        var objects = [];
        for (var i = 0; i < arguments.length; i++) {
            objects.push(inspect(arguments$1[i]));
        }
        return objects.join(' ');
    }

    var i = 1;
    var args = arguments;
    var len = args.length;
    var str = String(f).replace(formatRegExp, function(x) {
        if (x === '%%') { return '%'; }
        if (i >= len) { return x; }
        switch (x) {
            case '%s': return String(args[i++]);
            case '%d': return Number(args[i++]);
            case '%j':
                try {
                    return JSON.stringify(args[i++]);
                } catch (_) {
                    return '[Circular]';
                }
            default:
                return x;
        }
    });
    for (var x = args[i]; i < len; x = args[++i]) {
        if (isNull(x) || !isObject$1(x)) {
            str += ' ' + x;
        } else {
            str += ' ' + inspect(x);
        }
    }
    return str;
}


// Mark that a method should not be used.
// Returns a modified function which warns once by default.
// If --no-deprecation is set, then it is a no-op.
function deprecate(fn, msg) {
    // Allow for deprecating things in the process of starting up.
    if (isUndefined(global.process)) {
        return function() {
            return deprecate(fn, msg).apply(this, arguments);
        };
    }

    if (process$1.noDeprecation === true) {
        return fn;
    }

    var warned = false;
    function deprecated() {
        if (!warned) {
            if (process$1.throwDeprecation) {
                throw new Error(msg);
            } else if (process$1.traceDeprecation) {
                console.trace(msg);
            } else {
                console.error(msg);
            }
            warned = true;
        }
        return fn.apply(this, arguments);
    }

    return deprecated;
}


var debugs = {};
var debugEnviron;
function debuglog(set) {
    if (isUndefined(debugEnviron))
    { debugEnviron = process$1.env.NODE_DEBUG || ''; }
    set = set.toUpperCase();
    if (!debugs[set]) {
        if (new RegExp('\\b' + set + '\\b', 'i').test(debugEnviron)) {
            var pid = 0;
            debugs[set] = function() {
                var msg = format.apply(null, arguments);
                console.error('%s %d: %s', set, pid, msg);
            };
        } else {
            debugs[set] = function() {};
        }
    }
    return debugs[set];
}


/**
 * Echos the value of a value. Trys to print the value out
 * in the best way possible given the different types.
 *
 * @param {Object} obj The object to print out.
 * @param {Object} opts Optional options object that alters the output.
 */
/* legacy: obj, showHidden, depth, colors*/
function inspect(obj, opts) {
    // default options
    var ctx = {
        seen: [],
        stylize: stylizeNoColor
    };
    // legacy...
    if (arguments.length >= 3) { ctx.depth = arguments[2]; }
    if (arguments.length >= 4) { ctx.colors = arguments[3]; }
    if (isBoolean(opts)) {
        // legacy...
        ctx.showHidden = opts;
    } else if (opts) {
        // got an "options" object
        _extend(ctx, opts);
    }
    // set default options
    if (isUndefined(ctx.showHidden)) { ctx.showHidden = false; }
    if (isUndefined(ctx.depth)) { ctx.depth = 2; }
    if (isUndefined(ctx.colors)) { ctx.colors = false; }
    if (isUndefined(ctx.customInspect)) { ctx.customInspect = true; }
    if (ctx.colors) { ctx.stylize = stylizeWithColor; }
    return formatValue(ctx, obj, ctx.depth);
}

// http://en.wikipedia.org/wiki/ANSI_escape_code#graphics
inspect.colors = {
    'bold' : [1, 22],
    'italic' : [3, 23],
    'underline' : [4, 24],
    'inverse' : [7, 27],
    'white' : [37, 39],
    'grey' : [90, 39],
    'black' : [30, 39],
    'blue' : [34, 39],
    'cyan' : [36, 39],
    'green' : [32, 39],
    'magenta' : [35, 39],
    'red' : [31, 39],
    'yellow' : [33, 39]
};

// Don't use 'blue' not visible on cmd.exe
inspect.styles = {
    'special': 'cyan',
    'number': 'yellow',
    'boolean': 'yellow',
    'undefined': 'grey',
    'null': 'bold',
    'string': 'green',
    'date': 'magenta',
    // "name": intentionally not styling
    'regexp': 'red'
};


function stylizeWithColor(str, styleType) {
    var style = inspect.styles[styleType];

    if (style) {
        return '\u001b[' + inspect.colors[style][0] + 'm' + str +
            '\u001b[' + inspect.colors[style][1] + 'm';
    } else {
        return str;
    }
}


function stylizeNoColor(str, styleType) {
    return str;
}


function arrayToHash(array) {
    var hash = {};

    array.forEach(function(val, idx) {
        hash[val] = true;
    });

    return hash;
}


function formatValue(ctx, value, recurseTimes) {
    // Provide a hook for user-specified inspect functions.
    // Check that value is an object with an inspect function on it
    if (ctx.customInspect &&
        value &&
        isFunction(value.inspect) &&
        // Filter out the util module, it's inspect function is special
        value.inspect !== inspect &&
        // Also filter out any prototype objects using the circular check.
        !(value.constructor && value.constructor.prototype === value)) {
        var ret = value.inspect(recurseTimes, ctx);
        if (!isString(ret)) {
            ret = formatValue(ctx, ret, recurseTimes);
        }
        return ret;
    }

    // Primitive types cannot have properties
    var primitive = formatPrimitive(ctx, value);
    if (primitive) {
        return primitive;
    }

    // Look up the keys of the object.
    var keys = Object.keys(value);
    var visibleKeys = arrayToHash(keys);

    if (ctx.showHidden) {
        keys = Object.getOwnPropertyNames(value);
    }

    // IE doesn't make error fields non-enumerable
    // http://msdn.microsoft.com/en-us/library/ie/dww52sbt(v=vs.94).aspx
    if (isError(value)
        && (keys.indexOf('message') >= 0 || keys.indexOf('description') >= 0)) {
        return formatError(value);
    }

    // Some type of object without properties can be shortcutted.
    if (keys.length === 0) {
        if (isFunction(value)) {
            var name = value.name ? ': ' + value.name : '';
            return ctx.stylize('[Function' + name + ']', 'special');
        }
        if (isRegExp$1(value)) {
            return ctx.stylize(RegExp.prototype.toString.call(value), 'regexp');
        }
        if (isDate(value)) {
            return ctx.stylize(Date.prototype.toString.call(value), 'date');
        }
        if (isError(value)) {
            return formatError(value);
        }
    }

    var base = '', array = false, braces = ['{', '}'];

    // Make Array say that they are Array
    if (isArray(value)) {
        array = true;
        braces = ['[', ']'];
    }

    // Make functions say that they are functions
    if (isFunction(value)) {
        var n = value.name ? ': ' + value.name : '';
        base = ' [Function' + n + ']';
    }

    // Make RegExps say that they are RegExps
    if (isRegExp$1(value)) {
        base = ' ' + RegExp.prototype.toString.call(value);
    }

    // Make dates with properties first say the date
    if (isDate(value)) {
        base = ' ' + Date.prototype.toUTCString.call(value);
    }

    // Make error with message first say the error
    if (isError(value)) {
        base = ' ' + formatError(value);
    }

    if (keys.length === 0 && (!array || value.length == 0)) {
        return braces[0] + base + braces[1];
    }

    if (recurseTimes < 0) {
        if (isRegExp$1(value)) {
            return ctx.stylize(RegExp.prototype.toString.call(value), 'regexp');
        } else {
            return ctx.stylize('[Object]', 'special');
        }
    }

    ctx.seen.push(value);

    var output;
    if (array) {
        output = formatArray(ctx, value, recurseTimes, visibleKeys, keys);
    } else {
        output = keys.map(function(key) {
            return formatProperty(ctx, value, recurseTimes, visibleKeys, key, array);
        });
    }

    ctx.seen.pop();

    return reduceToSingleString(output, base, braces);
}


function formatPrimitive(ctx, value) {
    if (isUndefined(value))
    { return ctx.stylize('undefined', 'undefined'); }
    if (isString(value)) {
        var simple = '\'' + JSON.stringify(value).replace(/^"|"$/g, '')
            .replace(/'/g, "\\'")
            .replace(/\\"/g, '"') + '\'';
        return ctx.stylize(simple, 'string');
    }
    if (isNumber(value))
    { return ctx.stylize('' + value, 'number'); }
    if (isBoolean(value))
    { return ctx.stylize('' + value, 'boolean'); }
    // For some reason typeof null is "object", so special case here.
    if (isNull(value))
    { return ctx.stylize('null', 'null'); }
}


function formatError(value) {
    return '[' + Error.prototype.toString.call(value) + ']';
}


function formatArray(ctx, value, recurseTimes, visibleKeys, keys) {
    var output = [];
    for (var i = 0, l = value.length; i < l; ++i) {
        if (hasOwnProperty$1(value, String(i))) {
            output.push(formatProperty(ctx, value, recurseTimes, visibleKeys,
                String(i), true));
        } else {
            output.push('');
        }
    }
    keys.forEach(function(key) {
        if (!key.match(/^\d+$/)) {
            output.push(formatProperty(ctx, value, recurseTimes, visibleKeys,
                key, true));
        }
    });
    return output;
}


function formatProperty(ctx, value, recurseTimes, visibleKeys, key, array) {
    var name, str, desc;
    desc = Object.getOwnPropertyDescriptor(value, key) || { value: value[key] };
    if (desc.get) {
        if (desc.set) {
            str = ctx.stylize('[Getter/Setter]', 'special');
        } else {
            str = ctx.stylize('[Getter]', 'special');
        }
    } else {
        if (desc.set) {
            str = ctx.stylize('[Setter]', 'special');
        }
    }
    if (!hasOwnProperty$1(visibleKeys, key)) {
        name = '[' + key + ']';
    }
    if (!str) {
        if (ctx.seen.indexOf(desc.value) < 0) {
            if (isNull(recurseTimes)) {
                str = formatValue(ctx, desc.value, null);
            } else {
                str = formatValue(ctx, desc.value, recurseTimes - 1);
            }
            if (str.indexOf('\n') > -1) {
                if (array) {
                    str = str.split('\n').map(function(line) {
                        return '  ' + line;
                    }).join('\n').substr(2);
                } else {
                    str = '\n' + str.split('\n').map(function(line) {
                        return '   ' + line;
                    }).join('\n');
                }
            }
        } else {
            str = ctx.stylize('[Circular]', 'special');
        }
    }
    if (isUndefined(name)) {
        if (array && key.match(/^\d+$/)) {
            return str;
        }
        name = JSON.stringify('' + key);
        if (name.match(/^"([a-zA-Z_][a-zA-Z_0-9]*)"$/)) {
            name = name.substr(1, name.length - 2);
            name = ctx.stylize(name, 'name');
        } else {
            name = name.replace(/'/g, "\\'")
                .replace(/\\"/g, '"')
                .replace(/(^"|"$)/g, "'");
            name = ctx.stylize(name, 'string');
        }
    }

    return name + ': ' + str;
}


function reduceToSingleString(output, base, braces) {
    var numLinesEst = 0;
    var length = output.reduce(function(prev, cur) {
        numLinesEst++;
        if (cur.indexOf('\n') >= 0) { numLinesEst++; }
        return prev + cur.replace(/\u001b\[\d\d?m/g, '').length + 1;
    }, 0);

    if (length > 60) {
        return braces[0] +
            (base === '' ? '' : base + '\n ') +
            ' ' +
            output.join(',\n  ') +
            ' ' +
            braces[1];
    }

    return braces[0] + base + ' ' + output.join(', ') + ' ' + braces[1];
}


// NOTE: These type checking functions intentionally don't use `instanceof`
// because it is fragile and can be easily faked with `Object.create()`.
function isArray(ar) {
    return Array.isArray(ar);
}

function isBoolean(arg) {
    return typeof arg === 'boolean';
}

function isNull(arg) {
    return arg === null;
}

function isNullOrUndefined(arg) {
    return arg == null;
}

function isNumber(arg) {
    return typeof arg === 'number';
}

function isString(arg) {
    return typeof arg === 'string';
}

function isSymbol(arg) {
    return typeof arg === 'symbol';
}

function isUndefined(arg) {
    return arg === void 0;
}

function isRegExp$1(re) {
    return isObject$1(re) && objectToString(re) === '[object RegExp]';
}

function isObject$1(arg) {
    return typeof arg === 'object' && arg !== null;
}

function isDate(d) {
    return isObject$1(d) && objectToString(d) === '[object Date]';
}

function isError(e) {
    return isObject$1(e) &&
        (objectToString(e) === '[object Error]' || e instanceof Error);
}

function isFunction(arg) {
    return typeof arg === 'function';
}

function isPrimitive$1(arg) {
    return arg === null ||
        typeof arg === 'boolean' ||
        typeof arg === 'number' ||
        typeof arg === 'string' ||
        typeof arg === 'symbol' ||  // ES6 symbol
        typeof arg === 'undefined';
}

function isBuffer(maybeBuf) {
    return Buffer.isBuffer(maybeBuf);
}

function objectToString(o) {
    return Object.prototype.toString.call(o);
}


function pad(n) {
    return n < 10 ? '0' + n.toString(10) : n.toString(10);
}


var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep',
    'Oct', 'Nov', 'Dec'];

// 26 Feb 16:19:34
function timestamp() {
    var d = new Date();
    var time = [pad(d.getHours()),
        pad(d.getMinutes()),
        pad(d.getSeconds())].join(':');
    return [d.getDate(), months[d.getMonth()], time].join(' ');
}


// log is just a thin wrapper to console.log that prepends a timestamp
function log() {
    console.log('%s - %s', timestamp(), format.apply(null, arguments));
}


/**
 * Inherit the prototype methods from one constructor into another.
 *
 * The Function.prototype.inherits from lang.js rewritten as a standalone
 * function (not on Function.prototype). NOTE: If this file is to be loaded
 * during bootstrapping this function needs to be rewritten using some native
 * functions as prototype setup using normal JavaScript does not work as
 * expected during bootstrapping (see mirror.js in r114903).
 *
 * @param {function} ctor Constructor function which needs to inherit the
 *     prototype.
 * @param {function} superCtor Constructor function to inherit prototype from.
 */
function _extend(origin, add) {
    // Don't do anything if add isn't an object
    if (!add || !isObject$1(add)) { return origin; }

    var keys = Object.keys(add);
    var i = keys.length;
    while (i--) {
        origin[keys[i]] = add[keys[i]];
    }
    return origin;
}

function hasOwnProperty$1(obj, prop) {
    return Object.prototype.hasOwnProperty.call(obj, prop);
}

var util = {
    inherits: inherits$1,
    _extend: _extend,
    log: log,
    isBuffer: isBuffer,
    isPrimitive: isPrimitive$1,
    isFunction: isFunction,
    isError: isError,
    isDate: isDate,
    isObject: isObject$1,
    isRegExp: isRegExp$1,
    isUndefined: isUndefined,
    isSymbol: isSymbol,
    isString: isString,
    isNumber: isNumber,
    isNullOrUndefined: isNullOrUndefined,
    isNull: isNull,
    isBoolean: isBoolean,
    isArray: isArray,
    inspect: inspect,
    deprecate: deprecate,
    format: format,
    debuglog: debuglog
};


var util$1 = Object.freeze({
    format: format,
    deprecate: deprecate,
    debuglog: debuglog,
    inspect: inspect,
    isArray: isArray,
    isBoolean: isBoolean,
    isNull: isNull,
    isNullOrUndefined: isNullOrUndefined,
    isNumber: isNumber,
    isString: isString,
    isSymbol: isSymbol,
    isUndefined: isUndefined,
    isRegExp: isRegExp$1,
    isObject: isObject$1,
    isDate: isDate,
    isError: isError,
    isFunction: isFunction,
    isPrimitive: isPrimitive$1,
    isBuffer: isBuffer,
    log: log,
    inherits: inherits$1,
    _extend: _extend,
    default: util
});

var lookup = [];
var revLookup = [];
var Arr = typeof Uint8Array !== 'undefined' ? Uint8Array : Array;
var inited = false;
function init () {
    inited = true;
    var code = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';
    for (var i = 0, len = code.length; i < len; ++i) {
        lookup[i] = code[i];
        revLookup[code.charCodeAt(i)] = i;
    }

    revLookup['-'.charCodeAt(0)] = 62;
    revLookup['_'.charCodeAt(0)] = 63;
}

function toByteArray (b64) {
    if (!inited) {
        init();
    }
    var i, j, l, tmp, placeHolders, arr;
    var len = b64.length;

    if (len % 4 > 0) {
        throw new Error('Invalid string. Length must be a multiple of 4')
    }

    // the number of equal signs (place holders)
    // if there are two placeholders, than the two characters before it
    // represent one byte
    // if there is only one, then the three characters before it represent 2 bytes
    // this is just a cheap hack to not do indexOf twice
    placeHolders = b64[len - 2] === '=' ? 2 : b64[len - 1] === '=' ? 1 : 0;

    // base64 is 4/3 + up to two characters of the original data
    arr = new Arr(len * 3 / 4 - placeHolders);

    // if there are placeholders, only get up to the last complete 4 chars
    l = placeHolders > 0 ? len - 4 : len;

    var L = 0;

    for (i = 0, j = 0; i < l; i += 4, j += 3) {
        tmp = (revLookup[b64.charCodeAt(i)] << 18) | (revLookup[b64.charCodeAt(i + 1)] << 12) | (revLookup[b64.charCodeAt(i + 2)] << 6) | revLookup[b64.charCodeAt(i + 3)];
        arr[L++] = (tmp >> 16) & 0xFF;
        arr[L++] = (tmp >> 8) & 0xFF;
        arr[L++] = tmp & 0xFF;
    }

    if (placeHolders === 2) {
        tmp = (revLookup[b64.charCodeAt(i)] << 2) | (revLookup[b64.charCodeAt(i + 1)] >> 4);
        arr[L++] = tmp & 0xFF;
    } else if (placeHolders === 1) {
        tmp = (revLookup[b64.charCodeAt(i)] << 10) | (revLookup[b64.charCodeAt(i + 1)] << 4) | (revLookup[b64.charCodeAt(i + 2)] >> 2);
        arr[L++] = (tmp >> 8) & 0xFF;
        arr[L++] = tmp & 0xFF;
    }

    return arr
}

function tripletToBase64 (num) {
    return lookup[num >> 18 & 0x3F] + lookup[num >> 12 & 0x3F] + lookup[num >> 6 & 0x3F] + lookup[num & 0x3F]
}

function encodeChunk (uint8, start, end) {
    var tmp;
    var output = [];
    for (var i = start; i < end; i += 3) {
        tmp = (uint8[i] << 16) + (uint8[i + 1] << 8) + (uint8[i + 2]);
        output.push(tripletToBase64(tmp));
    }
    return output.join('')
}

function fromByteArray (uint8) {
    if (!inited) {
        init();
    }
    var tmp;
    var len = uint8.length;
    var extraBytes = len % 3; // if we have 1 byte left, pad 2 bytes
    var output = '';
    var parts = [];
    var maxChunkLength = 16383; // must be multiple of 3

    // go through the array every three bytes, we'll deal with trailing stuff later
    for (var i = 0, len2 = len - extraBytes; i < len2; i += maxChunkLength) {
        parts.push(encodeChunk(uint8, i, (i + maxChunkLength) > len2 ? len2 : (i + maxChunkLength)));
    }

    // pad the end with zeros, but make sure to not forget the extra bytes
    if (extraBytes === 1) {
        tmp = uint8[len - 1];
        output += lookup[tmp >> 2];
        output += lookup[(tmp << 4) & 0x3F];
        output += '==';
    } else if (extraBytes === 2) {
        tmp = (uint8[len - 2] << 8) + (uint8[len - 1]);
        output += lookup[tmp >> 10];
        output += lookup[(tmp >> 4) & 0x3F];
        output += lookup[(tmp << 2) & 0x3F];
        output += '=';
    }

    parts.push(output);

    return parts.join('')
}

function read (buffer, offset, isLE, mLen, nBytes) {
    var e, m;
    var eLen = nBytes * 8 - mLen - 1;
    var eMax = (1 << eLen) - 1;
    var eBias = eMax >> 1;
    var nBits = -7;
    var i = isLE ? (nBytes - 1) : 0;
    var d = isLE ? -1 : 1;
    var s = buffer[offset + i];

    i += d;

    e = s & ((1 << (-nBits)) - 1);
    s >>= (-nBits);
    nBits += eLen;
    for (; nBits > 0; e = e * 256 + buffer[offset + i], i += d, nBits -= 8) {}

    m = e & ((1 << (-nBits)) - 1);
    e >>= (-nBits);
    nBits += mLen;
    for (; nBits > 0; m = m * 256 + buffer[offset + i], i += d, nBits -= 8) {}

    if (e === 0) {
        e = 1 - eBias;
    } else if (e === eMax) {
        return m ? NaN : ((s ? -1 : 1) * Infinity)
    } else {
        m = m + Math.pow(2, mLen);
        e = e - eBias;
    }
    return (s ? -1 : 1) * m * Math.pow(2, e - mLen)
}

function write (buffer, value, offset, isLE, mLen, nBytes) {
    var e, m, c;
    var eLen = nBytes * 8 - mLen - 1;
    var eMax = (1 << eLen) - 1;
    var eBias = eMax >> 1;
    var rt = (mLen === 23 ? Math.pow(2, -24) - Math.pow(2, -77) : 0);
    var i = isLE ? 0 : (nBytes - 1);
    var d = isLE ? 1 : -1;
    var s = value < 0 || (value === 0 && 1 / value < 0) ? 1 : 0;

    value = Math.abs(value);

    if (isNaN(value) || value === Infinity) {
        m = isNaN(value) ? 1 : 0;
        e = eMax;
    } else {
        e = Math.floor(Math.log(value) / Math.LN2);
        if (value * (c = Math.pow(2, -e)) < 1) {
            e--;
            c *= 2;
        }
        if (e + eBias >= 1) {
            value += rt / c;
        } else {
            value += rt * Math.pow(2, 1 - eBias);
        }
        if (value * c >= 2) {
            e++;
            c /= 2;
        }

        if (e + eBias >= eMax) {
            m = 0;
            e = eMax;
        } else if (e + eBias >= 1) {
            m = (value * c - 1) * Math.pow(2, mLen);
            e = e + eBias;
        } else {
            m = value * Math.pow(2, eBias - 1) * Math.pow(2, mLen);
            e = 0;
        }
    }

    for (; mLen >= 8; buffer[offset + i] = m & 0xff, i += d, m /= 256, mLen -= 8) {}

    e = (e << mLen) | m;
    eLen += mLen;
    for (; eLen > 0; buffer[offset + i] = e & 0xff, i += d, e /= 256, eLen -= 8) {}

    buffer[offset + i - d] |= s * 128;
}

var toString$1 = {}.toString;

var isArray$1 = Array.isArray || function (arr) {
    return toString$1.call(arr) == '[object Array]';
};

/*!
 * The buffer module from node.js, for the browser.
 *
 * @author   Feross Aboukhadijeh <feross@feross.org> <http://feross.org>
 * @license  MIT
 */
/* eslint-disable no-proto */


var INSPECT_MAX_BYTES = 50;

/**
 * If `Buffer.TYPED_ARRAY_SUPPORT`:
 *   === true    Use Uint8Array implementation (fastest)
 *   === false   Use Object implementation (most compatible, even IE6)
 *
 * Browsers that support typed arrays are IE 10+, Firefox 4+, Chrome 7+, Safari 5.1+,
 * Opera 11.6+, iOS 4.2+.
 *
 * Due to various browser bugs, sometimes the Object implementation will be used even
 * when the browser supports typed arrays.
 *
 * Note:
 *
 *   - Firefox 4-29 lacks support for adding new properties to `Uint8Array` instances,
 *     See: https://bugzilla.mozilla.org/show_bug.cgi?id=695438.
 *
 *   - Chrome 9-10 is missing the `TypedArray.prototype.subarray` function.
 *
 *   - IE10 has a broken `TypedArray.prototype.subarray` function which returns arrays of
 *     incorrect length in some situations.

 * We detect these buggy browsers and set `Buffer.TYPED_ARRAY_SUPPORT` to `false` so they
 * get the Object implementation, which is slower but behaves correctly.
 */
Buffer$1.TYPED_ARRAY_SUPPORT = global.TYPED_ARRAY_SUPPORT !== undefined
    ? global.TYPED_ARRAY_SUPPORT
    : true;

function kMaxLength () {
    return Buffer$1.TYPED_ARRAY_SUPPORT
        ? 0x7fffffff
        : 0x3fffffff
}

function createBuffer (that, length) {
    if (kMaxLength() < length) {
        throw new RangeError('Invalid typed array length')
    }
    if (Buffer$1.TYPED_ARRAY_SUPPORT) {
        // Return an augmented `Uint8Array` instance, for best performance
        that = new Uint8Array(length);
        that.__proto__ = Buffer$1.prototype;
    } else {
        // Fallback: Return an object instance of the Buffer class
        if (that === null) {
            that = new Buffer$1(length);
        }
        that.length = length;
    }

    return that
}

/**
 * The Buffer constructor returns instances of `Uint8Array` that have their
 * prototype changed to `Buffer.prototype`. Furthermore, `Buffer` is a subclass of
 * `Uint8Array`, so the returned instances will have all the node `Buffer` methods
 * and the `Uint8Array` methods. Square bracket notation works as expected -- it
 * returns a single octet.
 *
 * The `Uint8Array` prototype remains unmodified.
 */

function Buffer$1 (arg, encodingOrOffset, length) {
    if (!Buffer$1.TYPED_ARRAY_SUPPORT && !(this instanceof Buffer$1)) {
        return new Buffer$1(arg, encodingOrOffset, length)
    }

    // Common case.
    if (typeof arg === 'number') {
        if (typeof encodingOrOffset === 'string') {
            throw new Error(
                'If encoding is specified then the first argument must be a string'
            )
        }
        return allocUnsafe(this, arg)
    }
    return from(this, arg, encodingOrOffset, length)
}

Buffer$1.poolSize = 8192; // not used by this implementation

// TODO: Legacy, not needed anymore. Remove in next major version.
Buffer$1._augment = function (arr) {
    arr.__proto__ = Buffer$1.prototype;
    return arr
};

function from (that, value, encodingOrOffset, length) {
    if (typeof value === 'number') {
        throw new TypeError('"value" argument must not be a number')
    }

    if (typeof ArrayBuffer !== 'undefined' && value instanceof ArrayBuffer) {
        return fromArrayBuffer(that, value, encodingOrOffset, length)
    }

    if (typeof value === 'string') {
        return fromString(that, value, encodingOrOffset)
    }

    return fromObject(that, value)
}

/**
 * Functionally equivalent to Buffer(arg, encoding) but throws a TypeError
 * if value is a number.
 * Buffer.from(str[, encoding])
 * Buffer.from(array)
 * Buffer.from(buffer)
 * Buffer.from(arrayBuffer[, byteOffset[, length]])
 **/
Buffer$1.from = function (value, encodingOrOffset, length) {
    return from(null, value, encodingOrOffset, length)
};

if (Buffer$1.TYPED_ARRAY_SUPPORT) {
    Buffer$1.prototype.__proto__ = Uint8Array.prototype;
    Buffer$1.__proto__ = Uint8Array;
    if (typeof Symbol !== 'undefined' && Symbol.species &&
        Buffer$1[Symbol.species] === Buffer$1) {
        // Fix subarray() in ES2016. See: https://github.com/feross/buffer/pull/97
        // Object.defineProperty(Buffer, Symbol.species, {
        //   value: null,
        //   configurable: true
        // })
    }
}

function assertSize (size) {
    if (typeof size !== 'number') {
        throw new TypeError('"size" argument must be a number')
    } else if (size < 0) {
        throw new RangeError('"size" argument must not be negative')
    }
}

function alloc (that, size, fill, encoding) {
    assertSize(size);
    if (size <= 0) {
        return createBuffer(that, size)
    }
    if (fill !== undefined) {
        // Only pay attention to encoding if it's a string. This
        // prevents accidentally sending in a number that would
        // be interpretted as a start offset.
        return typeof encoding === 'string'
            ? createBuffer(that, size).fill(fill, encoding)
            : createBuffer(that, size).fill(fill)
    }
    return createBuffer(that, size)
}

/**
 * Creates a new filled Buffer instance.
 * alloc(size[, fill[, encoding]])
 **/
Buffer$1.alloc = function (size, fill, encoding) {
    return alloc(null, size, fill, encoding)
};

function allocUnsafe (that, size) {
    assertSize(size);
    that = createBuffer(that, size < 0 ? 0 : checked(size) | 0);
    if (!Buffer$1.TYPED_ARRAY_SUPPORT) {
        for (var i = 0; i < size; ++i) {
            that[i] = 0;
        }
    }
    return that
}

/**
 * Equivalent to Buffer(num), by default creates a non-zero-filled Buffer instance.
 * */
Buffer$1.allocUnsafe = function (size) {
    return allocUnsafe(null, size)
};
/**
 * Equivalent to SlowBuffer(num), by default creates a non-zero-filled Buffer instance.
 */
Buffer$1.allocUnsafeSlow = function (size) {
    return allocUnsafe(null, size)
};

function fromString (that, string, encoding) {
    if (typeof encoding !== 'string' || encoding === '') {
        encoding = 'utf8';
    }

    if (!Buffer$1.isEncoding(encoding)) {
        throw new TypeError('"encoding" must be a valid string encoding')
    }

    var length = byteLength(string, encoding) | 0;
    that = createBuffer(that, length);

    var actual = that.write(string, encoding);

    if (actual !== length) {
        // Writing a hex string, for example, that contains invalid characters will
        // cause everything after the first invalid character to be ignored. (e.g.
        // 'abxxcd' will be treated as 'ab')
        that = that.slice(0, actual);
    }

    return that
}

function fromArrayLike (that, array) {
    var length = array.length < 0 ? 0 : checked(array.length) | 0;
    that = createBuffer(that, length);
    for (var i = 0; i < length; i += 1) {
        that[i] = array[i] & 255;
    }
    return that
}

function fromArrayBuffer (that, array, byteOffset, length) {
    array.byteLength; // this throws if `array` is not a valid ArrayBuffer

    if (byteOffset < 0 || array.byteLength < byteOffset) {
        throw new RangeError('\'offset\' is out of bounds')
    }

    if (array.byteLength < byteOffset + (length || 0)) {
        throw new RangeError('\'length\' is out of bounds')
    }

    if (byteOffset === undefined && length === undefined) {
        array = new Uint8Array(array);
    } else if (length === undefined) {
        array = new Uint8Array(array, byteOffset);
    } else {
        array = new Uint8Array(array, byteOffset, length);
    }

    if (Buffer$1.TYPED_ARRAY_SUPPORT) {
        // Return an augmented `Uint8Array` instance, for best performance
        that = array;
        that.__proto__ = Buffer$1.prototype;
    } else {
        // Fallback: Return an object instance of the Buffer class
        that = fromArrayLike(that, array);
    }
    return that
}

function fromObject (that, obj) {
    if (internalIsBuffer(obj)) {
        var len = checked(obj.length) | 0;
        that = createBuffer(that, len);

        if (that.length === 0) {
            return that
        }

        obj.copy(that, 0, 0, len);
        return that
    }

    if (obj) {
        if ((typeof ArrayBuffer !== 'undefined' &&
            obj.buffer instanceof ArrayBuffer) || 'length' in obj) {
            if (typeof obj.length !== 'number' || isnan(obj.length)) {
                return createBuffer(that, 0)
            }
            return fromArrayLike(that, obj)
        }

        if (obj.type === 'Buffer' && isArray$1(obj.data)) {
            return fromArrayLike(that, obj.data)
        }
    }

    throw new TypeError('First argument must be a string, Buffer, ArrayBuffer, Array, or array-like object.')
}

function checked (length) {
    // Note: cannot use `length < kMaxLength()` here because that fails when
    // length is NaN (which is otherwise coerced to zero.)
    if (length >= kMaxLength()) {
        throw new RangeError('Attempt to allocate Buffer larger than maximum ' +
            'size: 0x' + kMaxLength().toString(16) + ' bytes')
    }
    return length | 0
}


Buffer$1.isBuffer = isBuffer$1;
function internalIsBuffer (b) {
    return !!(b != null && b._isBuffer)
}

Buffer$1.compare = function compare (a, b) {
    if (!internalIsBuffer(a) || !internalIsBuffer(b)) {
        throw new TypeError('Arguments must be Buffers')
    }

    if (a === b) { return 0 }

    var x = a.length;
    var y = b.length;

    for (var i = 0, len = Math.min(x, y); i < len; ++i) {
        if (a[i] !== b[i]) {
            x = a[i];
            y = b[i];
            break
        }
    }

    if (x < y) { return -1 }
    if (y < x) { return 1 }
    return 0
};

Buffer$1.isEncoding = function isEncoding (encoding) {
    switch (String(encoding).toLowerCase()) {
        case 'hex':
        case 'utf8':
        case 'utf-8':
        case 'ascii':
        case 'latin1':
        case 'binary':
        case 'base64':
        case 'ucs2':
        case 'ucs-2':
        case 'utf16le':
        case 'utf-16le':
            return true
        default:
            return false
    }
};

Buffer$1.concat = function concat (list, length) {
    if (!isArray$1(list)) {
        throw new TypeError('"list" argument must be an Array of Buffers')
    }

    if (list.length === 0) {
        return Buffer$1.alloc(0)
    }

    var i;
    if (length === undefined) {
        length = 0;
        for (i = 0; i < list.length; ++i) {
            length += list[i].length;
        }
    }

    var buffer = Buffer$1.allocUnsafe(length);
    var pos = 0;
    for (i = 0; i < list.length; ++i) {
        var buf = list[i];
        if (!internalIsBuffer(buf)) {
            throw new TypeError('"list" argument must be an Array of Buffers')
        }
        buf.copy(buffer, pos);
        pos += buf.length;
    }
    return buffer
};

function byteLength (string, encoding) {
    if (internalIsBuffer(string)) {
        return string.length
    }
    if (typeof ArrayBuffer !== 'undefined' && typeof ArrayBuffer.isView === 'function' &&
        (ArrayBuffer.isView(string) || string instanceof ArrayBuffer)) {
        return string.byteLength
    }
    if (typeof string !== 'string') {
        string = '' + string;
    }

    var len = string.length;
    if (len === 0) { return 0 }

    // Use a for loop to avoid recursion
    var loweredCase = false;
    for (;;) {
        switch (encoding) {
            case 'ascii':
            case 'latin1':
            case 'binary':
                return len
            case 'utf8':
            case 'utf-8':
            case undefined:
                return utf8ToBytes(string).length
            case 'ucs2':
            case 'ucs-2':
            case 'utf16le':
            case 'utf-16le':
                return len * 2
            case 'hex':
                return len >>> 1
            case 'base64':
                return base64ToBytes(string).length
            default:
                if (loweredCase) { return utf8ToBytes(string).length } // assume utf8
                encoding = ('' + encoding).toLowerCase();
                loweredCase = true;
        }
    }
}
Buffer$1.byteLength = byteLength;

function slowToString (encoding, start, end) {
    var this$1 = this;

    var loweredCase = false;

    // No need to verify that "this.length <= MAX_UINT32" since it's a read-only
    // property of a typed array.

    // This behaves neither like String nor Uint8Array in that we set start/end
    // to their upper/lower bounds if the value passed is out of range.
    // undefined is handled specially as per ECMA-262 6th Edition,
    // Section 13.3.3.7 Runtime Semantics: KeyedBindingInitialization.
    if (start === undefined || start < 0) {
        start = 0;
    }
    // Return early if start > this.length. Done here to prevent potential uint32
    // coercion fail below.
    if (start > this.length) {
        return ''
    }

    if (end === undefined || end > this.length) {
        end = this.length;
    }

    if (end <= 0) {
        return ''
    }

    // Force coersion to uint32. This will also coerce falsey/NaN values to 0.
    end >>>= 0;
    start >>>= 0;

    if (end <= start) {
        return ''
    }

    if (!encoding) { encoding = 'utf8'; }

    while (true) {
        switch (encoding) {
            case 'hex':
                return hexSlice(this$1, start, end)

            case 'utf8':
            case 'utf-8':
                return utf8Slice(this$1, start, end)

            case 'ascii':
                return asciiSlice(this$1, start, end)

            case 'latin1':
            case 'binary':
                return latin1Slice(this$1, start, end)

            case 'base64':
                return base64Slice(this$1, start, end)

            case 'ucs2':
            case 'ucs-2':
            case 'utf16le':
            case 'utf-16le':
                return utf16leSlice(this$1, start, end)

            default:
                if (loweredCase) { throw new TypeError('Unknown encoding: ' + encoding) }
                encoding = (encoding + '').toLowerCase();
                loweredCase = true;
        }
    }
}

// The property is used by `Buffer.isBuffer` and `is-buffer` (in Safari 5-7) to detect
// Buffer instances.
Buffer$1.prototype._isBuffer = true;

function swap (b, n, m) {
    var i = b[n];
    b[n] = b[m];
    b[m] = i;
}

Buffer$1.prototype.swap16 = function swap16 () {
    var this$1 = this;

    var len = this.length;
    if (len % 2 !== 0) {
        throw new RangeError('Buffer size must be a multiple of 16-bits')
    }
    for (var i = 0; i < len; i += 2) {
        swap(this$1, i, i + 1);
    }
    return this
};

Buffer$1.prototype.swap32 = function swap32 () {
    var this$1 = this;

    var len = this.length;
    if (len % 4 !== 0) {
        throw new RangeError('Buffer size must be a multiple of 32-bits')
    }
    for (var i = 0; i < len; i += 4) {
        swap(this$1, i, i + 3);
        swap(this$1, i + 1, i + 2);
    }
    return this
};

Buffer$1.prototype.swap64 = function swap64 () {
    var this$1 = this;

    var len = this.length;
    if (len % 8 !== 0) {
        throw new RangeError('Buffer size must be a multiple of 64-bits')
    }
    for (var i = 0; i < len; i += 8) {
        swap(this$1, i, i + 7);
        swap(this$1, i + 1, i + 6);
        swap(this$1, i + 2, i + 5);
        swap(this$1, i + 3, i + 4);
    }
    return this
};

Buffer$1.prototype.toString = function toString () {
    var length = this.length | 0;
    if (length === 0) { return '' }
    if (arguments.length === 0) { return utf8Slice(this, 0, length) }
    return slowToString.apply(this, arguments)
};

Buffer$1.prototype.equals = function equals (b) {
    if (!internalIsBuffer(b)) { throw new TypeError('Argument must be a Buffer') }
    if (this === b) { return true }
    return Buffer$1.compare(this, b) === 0
};

Buffer$1.prototype.inspect = function inspect () {
    var str = '';
    var max = INSPECT_MAX_BYTES;
    if (this.length > 0) {
        str = this.toString('hex', 0, max).match(/.{2}/g).join(' ');
        if (this.length > max) { str += ' ... '; }
    }
    return '<Buffer ' + str + '>'
};

Buffer$1.prototype.compare = function compare (target, start, end, thisStart, thisEnd) {
    if (!internalIsBuffer(target)) {
        throw new TypeError('Argument must be a Buffer')
    }

    if (start === undefined) {
        start = 0;
    }
    if (end === undefined) {
        end = target ? target.length : 0;
    }
    if (thisStart === undefined) {
        thisStart = 0;
    }
    if (thisEnd === undefined) {
        thisEnd = this.length;
    }

    if (start < 0 || end > target.length || thisStart < 0 || thisEnd > this.length) {
        throw new RangeError('out of range index')
    }

    if (thisStart >= thisEnd && start >= end) {
        return 0
    }
    if (thisStart >= thisEnd) {
        return -1
    }
    if (start >= end) {
        return 1
    }

    start >>>= 0;
    end >>>= 0;
    thisStart >>>= 0;
    thisEnd >>>= 0;

    if (this === target) { return 0 }

    var x = thisEnd - thisStart;
    var y = end - start;
    var len = Math.min(x, y);

    var thisCopy = this.slice(thisStart, thisEnd);
    var targetCopy = target.slice(start, end);

    for (var i = 0; i < len; ++i) {
        if (thisCopy[i] !== targetCopy[i]) {
            x = thisCopy[i];
            y = targetCopy[i];
            break
        }
    }

    if (x < y) { return -1 }
    if (y < x) { return 1 }
    return 0
};

// Finds either the first index of `val` in `buffer` at offset >= `byteOffset`,
// OR the last index of `val` in `buffer` at offset <= `byteOffset`.
//
// Arguments:
// - buffer - a Buffer to search
// - val - a string, Buffer, or number
// - byteOffset - an index into `buffer`; will be clamped to an int32
// - encoding - an optional encoding, relevant is val is a string
// - dir - true for indexOf, false for lastIndexOf
function bidirectionalIndexOf (buffer, val, byteOffset, encoding, dir) {
    // Empty buffer means no match
    if (buffer.length === 0) { return -1 }

    // Normalize byteOffset
    if (typeof byteOffset === 'string') {
        encoding = byteOffset;
        byteOffset = 0;
    } else if (byteOffset > 0x7fffffff) {
        byteOffset = 0x7fffffff;
    } else if (byteOffset < -0x80000000) {
        byteOffset = -0x80000000;
    }
    byteOffset = +byteOffset;  // Coerce to Number.
    if (isNaN(byteOffset)) {
        // byteOffset: it it's undefined, null, NaN, "foo", etc, search whole buffer
        byteOffset = dir ? 0 : (buffer.length - 1);
    }

    // Normalize byteOffset: negative offsets start from the end of the buffer
    if (byteOffset < 0) { byteOffset = buffer.length + byteOffset; }
    if (byteOffset >= buffer.length) {
        if (dir) { return -1 }
        else { byteOffset = buffer.length - 1; }
    } else if (byteOffset < 0) {
        if (dir) { byteOffset = 0; }
        else { return -1 }
    }

    // Normalize val
    if (typeof val === 'string') {
        val = Buffer$1.from(val, encoding);
    }

    // Finally, search either indexOf (if dir is true) or lastIndexOf
    if (internalIsBuffer(val)) {
        // Special case: looking for empty string/buffer always fails
        if (val.length === 0) {
            return -1
        }
        return arrayIndexOf(buffer, val, byteOffset, encoding, dir)
    } else if (typeof val === 'number') {
        val = val & 0xFF; // Search for a byte value [0-255]
        if (Buffer$1.TYPED_ARRAY_SUPPORT &&
            typeof Uint8Array.prototype.indexOf === 'function') {
            if (dir) {
                return Uint8Array.prototype.indexOf.call(buffer, val, byteOffset)
            } else {
                return Uint8Array.prototype.lastIndexOf.call(buffer, val, byteOffset)
            }
        }
        return arrayIndexOf(buffer, [ val ], byteOffset, encoding, dir)
    }

    throw new TypeError('val must be string, number or Buffer')
}

function arrayIndexOf (arr, val, byteOffset, encoding, dir) {
    var indexSize = 1;
    var arrLength = arr.length;
    var valLength = val.length;

    if (encoding !== undefined) {
        encoding = String(encoding).toLowerCase();
        if (encoding === 'ucs2' || encoding === 'ucs-2' ||
            encoding === 'utf16le' || encoding === 'utf-16le') {
            if (arr.length < 2 || val.length < 2) {
                return -1
            }
            indexSize = 2;
            arrLength /= 2;
            valLength /= 2;
            byteOffset /= 2;
        }
    }

    function read$$1 (buf, i) {
        if (indexSize === 1) {
            return buf[i]
        } else {
            return buf.readUInt16BE(i * indexSize)
        }
    }

    var i;
    if (dir) {
        var foundIndex = -1;
        for (i = byteOffset; i < arrLength; i++) {
            if (read$$1(arr, i) === read$$1(val, foundIndex === -1 ? 0 : i - foundIndex)) {
                if (foundIndex === -1) { foundIndex = i; }
                if (i - foundIndex + 1 === valLength) { return foundIndex * indexSize }
            } else {
                if (foundIndex !== -1) { i -= i - foundIndex; }
                foundIndex = -1;
            }
        }
    } else {
        if (byteOffset + valLength > arrLength) { byteOffset = arrLength - valLength; }
        for (i = byteOffset; i >= 0; i--) {
            var found = true;
            for (var j = 0; j < valLength; j++) {
                if (read$$1(arr, i + j) !== read$$1(val, j)) {
                    found = false;
                    break
                }
            }
            if (found) { return i }
        }
    }

    return -1
}

Buffer$1.prototype.includes = function includes (val, byteOffset, encoding) {
    return this.indexOf(val, byteOffset, encoding) !== -1
};

Buffer$1.prototype.indexOf = function indexOf (val, byteOffset, encoding) {
    return bidirectionalIndexOf(this, val, byteOffset, encoding, true)
};

Buffer$1.prototype.lastIndexOf = function lastIndexOf (val, byteOffset, encoding) {
    return bidirectionalIndexOf(this, val, byteOffset, encoding, false)
};

function hexWrite (buf, string, offset, length) {
    offset = Number(offset) || 0;
    var remaining = buf.length - offset;
    if (!length) {
        length = remaining;
    } else {
        length = Number(length);
        if (length > remaining) {
            length = remaining;
        }
    }

    // must be an even number of digits
    var strLen = string.length;
    if (strLen % 2 !== 0) { throw new TypeError('Invalid hex string') }

    if (length > strLen / 2) {
        length = strLen / 2;
    }
    for (var i = 0; i < length; ++i) {
        var parsed = parseInt(string.substr(i * 2, 2), 16);
        if (isNaN(parsed)) { return i }
        buf[offset + i] = parsed;
    }
    return i
}

function utf8Write (buf, string, offset, length) {
    return blitBuffer(utf8ToBytes(string, buf.length - offset), buf, offset, length)
}

function asciiWrite (buf, string, offset, length) {
    return blitBuffer(asciiToBytes(string), buf, offset, length)
}

function latin1Write (buf, string, offset, length) {
    return asciiWrite(buf, string, offset, length)
}

function base64Write (buf, string, offset, length) {
    return blitBuffer(base64ToBytes(string), buf, offset, length)
}

function ucs2Write (buf, string, offset, length) {
    return blitBuffer(utf16leToBytes(string, buf.length - offset), buf, offset, length)
}

Buffer$1.prototype.write = function write$$1 (string, offset, length, encoding) {
    var this$1 = this;

    // Buffer#write(string)
    if (offset === undefined) {
        encoding = 'utf8';
        length = this.length;
        offset = 0;
        // Buffer#write(string, encoding)
    } else if (length === undefined && typeof offset === 'string') {
        encoding = offset;
        length = this.length;
        offset = 0;
        // Buffer#write(string, offset[, length][, encoding])
    } else if (isFinite(offset)) {
        offset = offset | 0;
        if (isFinite(length)) {
            length = length | 0;
            if (encoding === undefined) { encoding = 'utf8'; }
        } else {
            encoding = length;
            length = undefined;
        }
        // legacy write(string, encoding, offset, length) - remove in v0.13
    } else {
        throw new Error(
            'Buffer.write(string, encoding, offset[, length]) is no longer supported'
        )
    }

    var remaining = this.length - offset;
    if (length === undefined || length > remaining) { length = remaining; }

    if ((string.length > 0 && (length < 0 || offset < 0)) || offset > this.length) {
        throw new RangeError('Attempt to write outside buffer bounds')
    }

    if (!encoding) { encoding = 'utf8'; }

    var loweredCase = false;
    for (;;) {
        switch (encoding) {
            case 'hex':
                return hexWrite(this$1, string, offset, length)

            case 'utf8':
            case 'utf-8':
                return utf8Write(this$1, string, offset, length)

            case 'ascii':
                return asciiWrite(this$1, string, offset, length)

            case 'latin1':
            case 'binary':
                return latin1Write(this$1, string, offset, length)

            case 'base64':
                // Warning: maxLength not taken into account in base64Write
                return base64Write(this$1, string, offset, length)

            case 'ucs2':
            case 'ucs-2':
            case 'utf16le':
            case 'utf-16le':
                return ucs2Write(this$1, string, offset, length)

            default:
                if (loweredCase) { throw new TypeError('Unknown encoding: ' + encoding) }
                encoding = ('' + encoding).toLowerCase();
                loweredCase = true;
        }
    }
};

Buffer$1.prototype.toJSON = function toJSON () {
    return {
        type: 'Buffer',
        data: Array.prototype.slice.call(this._arr || this, 0)
    }
};

function base64Slice (buf, start, end) {
    if (start === 0 && end === buf.length) {
        return fromByteArray(buf)
    } else {
        return fromByteArray(buf.slice(start, end))
    }
}

function utf8Slice (buf, start, end) {
    end = Math.min(buf.length, end);
    var res = [];

    var i = start;
    while (i < end) {
        var firstByte = buf[i];
        var codePoint = null;
        var bytesPerSequence = (firstByte > 0xEF) ? 4
            : (firstByte > 0xDF) ? 3
                : (firstByte > 0xBF) ? 2
                    : 1;

        if (i + bytesPerSequence <= end) {
            var secondByte, thirdByte, fourthByte, tempCodePoint;

            switch (bytesPerSequence) {
                case 1:
                    if (firstByte < 0x80) {
                        codePoint = firstByte;
                    }
                    break
                case 2:
                    secondByte = buf[i + 1];
                    if ((secondByte & 0xC0) === 0x80) {
                        tempCodePoint = (firstByte & 0x1F) << 0x6 | (secondByte & 0x3F);
                        if (tempCodePoint > 0x7F) {
                            codePoint = tempCodePoint;
                        }
                    }
                    break
                case 3:
                    secondByte = buf[i + 1];
                    thirdByte = buf[i + 2];
                    if ((secondByte & 0xC0) === 0x80 && (thirdByte & 0xC0) === 0x80) {
                        tempCodePoint = (firstByte & 0xF) << 0xC | (secondByte & 0x3F) << 0x6 | (thirdByte & 0x3F);
                        if (tempCodePoint > 0x7FF && (tempCodePoint < 0xD800 || tempCodePoint > 0xDFFF)) {
                            codePoint = tempCodePoint;
                        }
                    }
                    break
                case 4:
                    secondByte = buf[i + 1];
                    thirdByte = buf[i + 2];
                    fourthByte = buf[i + 3];
                    if ((secondByte & 0xC0) === 0x80 && (thirdByte & 0xC0) === 0x80 && (fourthByte & 0xC0) === 0x80) {
                        tempCodePoint = (firstByte & 0xF) << 0x12 | (secondByte & 0x3F) << 0xC | (thirdByte & 0x3F) << 0x6 | (fourthByte & 0x3F);
                        if (tempCodePoint > 0xFFFF && tempCodePoint < 0x110000) {
                            codePoint = tempCodePoint;
                        }
                    }
            }
        }

        if (codePoint === null) {
            // we did not generate a valid codePoint so insert a
            // replacement char (U+FFFD) and advance only 1 byte
            codePoint = 0xFFFD;
            bytesPerSequence = 1;
        } else if (codePoint > 0xFFFF) {
            // encode to utf16 (surrogate pair dance)
            codePoint -= 0x10000;
            res.push(codePoint >>> 10 & 0x3FF | 0xD800);
            codePoint = 0xDC00 | codePoint & 0x3FF;
        }

        res.push(codePoint);
        i += bytesPerSequence;
    }

    return decodeCodePointsArray(res)
}

// Based on http://stackoverflow.com/a/22747272/680742, the browser with
// the lowest limit is Chrome, with 0x10000 args.
// We go 1 magnitude less, for safety
var MAX_ARGUMENTS_LENGTH = 0x1000;

function decodeCodePointsArray (codePoints) {
    var len = codePoints.length;
    if (len <= MAX_ARGUMENTS_LENGTH) {
        return String.fromCharCode.apply(String, codePoints) // avoid extra slice()
    }

    // Decode in chunks to avoid "call stack size exceeded".
    var res = '';
    var i = 0;
    while (i < len) {
        res += String.fromCharCode.apply(
            String,
            codePoints.slice(i, i += MAX_ARGUMENTS_LENGTH)
        );
    }
    return res
}

function asciiSlice (buf, start, end) {
    var ret = '';
    end = Math.min(buf.length, end);

    for (var i = start; i < end; ++i) {
        ret += String.fromCharCode(buf[i] & 0x7F);
    }
    return ret
}

function latin1Slice (buf, start, end) {
    var ret = '';
    end = Math.min(buf.length, end);

    for (var i = start; i < end; ++i) {
        ret += String.fromCharCode(buf[i]);
    }
    return ret
}

function hexSlice (buf, start, end) {
    var len = buf.length;

    if (!start || start < 0) { start = 0; }
    if (!end || end < 0 || end > len) { end = len; }

    var out = '';
    for (var i = start; i < end; ++i) {
        out += toHex(buf[i]);
    }
    return out
}

function utf16leSlice (buf, start, end) {
    var bytes = buf.slice(start, end);
    var res = '';
    for (var i = 0; i < bytes.length; i += 2) {
        res += String.fromCharCode(bytes[i] + bytes[i + 1] * 256);
    }
    return res
}

Buffer$1.prototype.slice = function slice (start, end) {
    var this$1 = this;

    var len = this.length;
    start = ~~start;
    end = end === undefined ? len : ~~end;

    if (start < 0) {
        start += len;
        if (start < 0) { start = 0; }
    } else if (start > len) {
        start = len;
    }

    if (end < 0) {
        end += len;
        if (end < 0) { end = 0; }
    } else if (end > len) {
        end = len;
    }

    if (end < start) { end = start; }

    var newBuf;
    if (Buffer$1.TYPED_ARRAY_SUPPORT) {
        newBuf = this.subarray(start, end);
        newBuf.__proto__ = Buffer$1.prototype;
    } else {
        var sliceLen = end - start;
        newBuf = new Buffer$1(sliceLen, undefined);
        for (var i = 0; i < sliceLen; ++i) {
            newBuf[i] = this$1[i + start];
        }
    }

    return newBuf
};

/*
 * Need to make sure that buffer isn't trying to write out of bounds.
 */
function checkOffset (offset, ext, length) {
    if ((offset % 1) !== 0 || offset < 0) { throw new RangeError('offset is not uint') }
    if (offset + ext > length) { throw new RangeError('Trying to access beyond buffer length') }
}

Buffer$1.prototype.readUIntLE = function readUIntLE (offset, byteLength, noAssert) {
    var this$1 = this;

    offset = offset | 0;
    byteLength = byteLength | 0;
    if (!noAssert) { checkOffset(offset, byteLength, this.length); }

    var val = this[offset];
    var mul = 1;
    var i = 0;
    while (++i < byteLength && (mul *= 0x100)) {
        val += this$1[offset + i] * mul;
    }

    return val
};

Buffer$1.prototype.readUIntBE = function readUIntBE (offset, byteLength, noAssert) {
    var this$1 = this;

    offset = offset | 0;
    byteLength = byteLength | 0;
    if (!noAssert) {
        checkOffset(offset, byteLength, this.length);
    }

    var val = this[offset + --byteLength];
    var mul = 1;
    while (byteLength > 0 && (mul *= 0x100)) {
        val += this$1[offset + --byteLength] * mul;
    }

    return val
};

Buffer$1.prototype.readUInt8 = function readUInt8 (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 1, this.length); }
    return this[offset]
};

Buffer$1.prototype.readUInt16LE = function readUInt16LE (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 2, this.length); }
    return this[offset] | (this[offset + 1] << 8)
};

Buffer$1.prototype.readUInt16BE = function readUInt16BE (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 2, this.length); }
    return (this[offset] << 8) | this[offset + 1]
};

Buffer$1.prototype.readUInt32LE = function readUInt32LE (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 4, this.length); }

    return ((this[offset]) |
        (this[offset + 1] << 8) |
        (this[offset + 2] << 16)) +
        (this[offset + 3] * 0x1000000)
};

Buffer$1.prototype.readUInt32BE = function readUInt32BE (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 4, this.length); }

    return (this[offset] * 0x1000000) +
        ((this[offset + 1] << 16) |
            (this[offset + 2] << 8) |
            this[offset + 3])
};

Buffer$1.prototype.readIntLE = function readIntLE (offset, byteLength, noAssert) {
    var this$1 = this;

    offset = offset | 0;
    byteLength = byteLength | 0;
    if (!noAssert) { checkOffset(offset, byteLength, this.length); }

    var val = this[offset];
    var mul = 1;
    var i = 0;
    while (++i < byteLength && (mul *= 0x100)) {
        val += this$1[offset + i] * mul;
    }
    mul *= 0x80;

    if (val >= mul) { val -= Math.pow(2, 8 * byteLength); }

    return val
};

Buffer$1.prototype.readIntBE = function readIntBE (offset, byteLength, noAssert) {
    var this$1 = this;

    offset = offset | 0;
    byteLength = byteLength | 0;
    if (!noAssert) { checkOffset(offset, byteLength, this.length); }

    var i = byteLength;
    var mul = 1;
    var val = this[offset + --i];
    while (i > 0 && (mul *= 0x100)) {
        val += this$1[offset + --i] * mul;
    }
    mul *= 0x80;

    if (val >= mul) { val -= Math.pow(2, 8 * byteLength); }

    return val
};

Buffer$1.prototype.readInt8 = function readInt8 (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 1, this.length); }
    if (!(this[offset] & 0x80)) { return (this[offset]) }
    return ((0xff - this[offset] + 1) * -1)
};

Buffer$1.prototype.readInt16LE = function readInt16LE (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 2, this.length); }
    var val = this[offset] | (this[offset + 1] << 8);
    return (val & 0x8000) ? val | 0xFFFF0000 : val
};

Buffer$1.prototype.readInt16BE = function readInt16BE (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 2, this.length); }
    var val = this[offset + 1] | (this[offset] << 8);
    return (val & 0x8000) ? val | 0xFFFF0000 : val
};

Buffer$1.prototype.readInt32LE = function readInt32LE (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 4, this.length); }

    return (this[offset]) |
        (this[offset + 1] << 8) |
        (this[offset + 2] << 16) |
        (this[offset + 3] << 24)
};

Buffer$1.prototype.readInt32BE = function readInt32BE (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 4, this.length); }

    return (this[offset] << 24) |
        (this[offset + 1] << 16) |
        (this[offset + 2] << 8) |
        (this[offset + 3])
};

Buffer$1.prototype.readFloatLE = function readFloatLE (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 4, this.length); }
    return read(this, offset, true, 23, 4)
};

Buffer$1.prototype.readFloatBE = function readFloatBE (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 4, this.length); }
    return read(this, offset, false, 23, 4)
};

Buffer$1.prototype.readDoubleLE = function readDoubleLE (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 8, this.length); }
    return read(this, offset, true, 52, 8)
};

Buffer$1.prototype.readDoubleBE = function readDoubleBE (offset, noAssert) {
    if (!noAssert) { checkOffset(offset, 8, this.length); }
    return read(this, offset, false, 52, 8)
};

function checkInt (buf, value, offset, ext, max, min) {
    if (!internalIsBuffer(buf)) { throw new TypeError('"buffer" argument must be a Buffer instance') }
    if (value > max || value < min) { throw new RangeError('"value" argument is out of bounds') }
    if (offset + ext > buf.length) { throw new RangeError('Index out of range') }
}

Buffer$1.prototype.writeUIntLE = function writeUIntLE (value, offset, byteLength, noAssert) {
    var this$1 = this;

    value = +value;
    offset = offset | 0;
    byteLength = byteLength | 0;
    if (!noAssert) {
        var maxBytes = Math.pow(2, 8 * byteLength) - 1;
        checkInt(this, value, offset, byteLength, maxBytes, 0);
    }

    var mul = 1;
    var i = 0;
    this[offset] = value & 0xFF;
    while (++i < byteLength && (mul *= 0x100)) {
        this$1[offset + i] = (value / mul) & 0xFF;
    }

    return offset + byteLength
};

Buffer$1.prototype.writeUIntBE = function writeUIntBE (value, offset, byteLength, noAssert) {
    var this$1 = this;

    value = +value;
    offset = offset | 0;
    byteLength = byteLength | 0;
    if (!noAssert) {
        var maxBytes = Math.pow(2, 8 * byteLength) - 1;
        checkInt(this, value, offset, byteLength, maxBytes, 0);
    }

    var i = byteLength - 1;
    var mul = 1;
    this[offset + i] = value & 0xFF;
    while (--i >= 0 && (mul *= 0x100)) {
        this$1[offset + i] = (value / mul) & 0xFF;
    }

    return offset + byteLength
};

Buffer$1.prototype.writeUInt8 = function writeUInt8 (value, offset, noAssert) {
    value = +value;
    offset = offset | 0;
    if (!noAssert) { checkInt(this, value, offset, 1, 0xff, 0); }
    if (!Buffer$1.TYPED_ARRAY_SUPPORT) { value = Math.floor(value); }
    this[offset] = (value & 0xff);
    return offset + 1
};

function objectWriteUInt16 (buf, value, offset, littleEndian) {
    if (value < 0) { value = 0xffff + value + 1; }
    for (var i = 0, j = Math.min(buf.length - offset, 2); i < j; ++i) {
        buf[offset + i] = (value & (0xff << (8 * (littleEndian ? i : 1 - i)))) >>>
            (littleEndian ? i : 1 - i) * 8;
    }
}

Buffer$1.prototype.writeUInt16LE = function writeUInt16LE (value, offset, noAssert) {
    value = +value;
    offset = offset | 0;
    if (!noAssert) { checkInt(this, value, offset, 2, 0xffff, 0); }
    if (Buffer$1.TYPED_ARRAY_SUPPORT) {
        this[offset] = (value & 0xff);
        this[offset + 1] = (value >>> 8);
    } else {
        objectWriteUInt16(this, value, offset, true);
    }
    return offset + 2
};

Buffer$1.prototype.writeUInt16BE = function writeUInt16BE (value, offset, noAssert) {
    value = +value;
    offset = offset | 0;
    if (!noAssert) { checkInt(this, value, offset, 2, 0xffff, 0); }
    if (Buffer$1.TYPED_ARRAY_SUPPORT) {
        this[offset] = (value >>> 8);
        this[offset + 1] = (value & 0xff);
    } else {
        objectWriteUInt16(this, value, offset, false);
    }
    return offset + 2
};

function objectWriteUInt32 (buf, value, offset, littleEndian) {
    if (value < 0) { value = 0xffffffff + value + 1; }
    for (var i = 0, j = Math.min(buf.length - offset, 4); i < j; ++i) {
        buf[offset + i] = (value >>> (littleEndian ? i : 3 - i) * 8) & 0xff;
    }
}

Buffer$1.prototype.writeUInt32LE = function writeUInt32LE (value, offset, noAssert) {
    value = +value;
    offset = offset | 0;
    if (!noAssert) { checkInt(this, value, offset, 4, 0xffffffff, 0); }
    if (Buffer$1.TYPED_ARRAY_SUPPORT) {
        this[offset + 3] = (value >>> 24);
        this[offset + 2] = (value >>> 16);
        this[offset + 1] = (value >>> 8);
        this[offset] = (value & 0xff);
    } else {
        objectWriteUInt32(this, value, offset, true);
    }
    return offset + 4
};

Buffer$1.prototype.writeUInt32BE = function writeUInt32BE (value, offset, noAssert) {
    value = +value;
    offset = offset | 0;
    if (!noAssert) { checkInt(this, value, offset, 4, 0xffffffff, 0); }
    if (Buffer$1.TYPED_ARRAY_SUPPORT) {
        this[offset] = (value >>> 24);
        this[offset + 1] = (value >>> 16);
        this[offset + 2] = (value >>> 8);
        this[offset + 3] = (value & 0xff);
    } else {
        objectWriteUInt32(this, value, offset, false);
    }
    return offset + 4
};

Buffer$1.prototype.writeIntLE = function writeIntLE (value, offset, byteLength, noAssert) {
    var this$1 = this;

    value = +value;
    offset = offset | 0;
    if (!noAssert) {
        var limit = Math.pow(2, 8 * byteLength - 1);

        checkInt(this, value, offset, byteLength, limit - 1, -limit);
    }

    var i = 0;
    var mul = 1;
    var sub = 0;
    this[offset] = value & 0xFF;
    while (++i < byteLength && (mul *= 0x100)) {
        if (value < 0 && sub === 0 && this$1[offset + i - 1] !== 0) {
            sub = 1;
        }
        this$1[offset + i] = ((value / mul) >> 0) - sub & 0xFF;
    }

    return offset + byteLength
};

Buffer$1.prototype.writeIntBE = function writeIntBE (value, offset, byteLength, noAssert) {
    var this$1 = this;

    value = +value;
    offset = offset | 0;
    if (!noAssert) {
        var limit = Math.pow(2, 8 * byteLength - 1);

        checkInt(this, value, offset, byteLength, limit - 1, -limit);
    }

    var i = byteLength - 1;
    var mul = 1;
    var sub = 0;
    this[offset + i] = value & 0xFF;
    while (--i >= 0 && (mul *= 0x100)) {
        if (value < 0 && sub === 0 && this$1[offset + i + 1] !== 0) {
            sub = 1;
        }
        this$1[offset + i] = ((value / mul) >> 0) - sub & 0xFF;
    }

    return offset + byteLength
};

Buffer$1.prototype.writeInt8 = function writeInt8 (value, offset, noAssert) {
    value = +value;
    offset = offset | 0;
    if (!noAssert) { checkInt(this, value, offset, 1, 0x7f, -0x80); }
    if (!Buffer$1.TYPED_ARRAY_SUPPORT) { value = Math.floor(value); }
    if (value < 0) { value = 0xff + value + 1; }
    this[offset] = (value & 0xff);
    return offset + 1
};

Buffer$1.prototype.writeInt16LE = function writeInt16LE (value, offset, noAssert) {
    value = +value;
    offset = offset | 0;
    if (!noAssert) { checkInt(this, value, offset, 2, 0x7fff, -0x8000); }
    if (Buffer$1.TYPED_ARRAY_SUPPORT) {
        this[offset] = (value & 0xff);
        this[offset + 1] = (value >>> 8);
    } else {
        objectWriteUInt16(this, value, offset, true);
    }
    return offset + 2
};

Buffer$1.prototype.writeInt16BE = function writeInt16BE (value, offset, noAssert) {
    value = +value;
    offset = offset | 0;
    if (!noAssert) { checkInt(this, value, offset, 2, 0x7fff, -0x8000); }
    if (Buffer$1.TYPED_ARRAY_SUPPORT) {
        this[offset] = (value >>> 8);
        this[offset + 1] = (value & 0xff);
    } else {
        objectWriteUInt16(this, value, offset, false);
    }
    return offset + 2
};

Buffer$1.prototype.writeInt32LE = function writeInt32LE (value, offset, noAssert) {
    value = +value;
    offset = offset | 0;
    if (!noAssert) { checkInt(this, value, offset, 4, 0x7fffffff, -0x80000000); }
    if (Buffer$1.TYPED_ARRAY_SUPPORT) {
        this[offset] = (value & 0xff);
        this[offset + 1] = (value >>> 8);
        this[offset + 2] = (value >>> 16);
        this[offset + 3] = (value >>> 24);
    } else {
        objectWriteUInt32(this, value, offset, true);
    }
    return offset + 4
};

Buffer$1.prototype.writeInt32BE = function writeInt32BE (value, offset, noAssert) {
    value = +value;
    offset = offset | 0;
    if (!noAssert) { checkInt(this, value, offset, 4, 0x7fffffff, -0x80000000); }
    if (value < 0) { value = 0xffffffff + value + 1; }
    if (Buffer$1.TYPED_ARRAY_SUPPORT) {
        this[offset] = (value >>> 24);
        this[offset + 1] = (value >>> 16);
        this[offset + 2] = (value >>> 8);
        this[offset + 3] = (value & 0xff);
    } else {
        objectWriteUInt32(this, value, offset, false);
    }
    return offset + 4
};

function checkIEEE754 (buf, value, offset, ext, max, min) {
    if (offset + ext > buf.length) { throw new RangeError('Index out of range') }
    if (offset < 0) { throw new RangeError('Index out of range') }
}

function writeFloat (buf, value, offset, littleEndian, noAssert) {
    if (!noAssert) {
        checkIEEE754(buf, value, offset, 4, 3.4028234663852886e+38, -3.4028234663852886e+38);
    }
    write(buf, value, offset, littleEndian, 23, 4);
    return offset + 4
}

Buffer$1.prototype.writeFloatLE = function writeFloatLE (value, offset, noAssert) {
    return writeFloat(this, value, offset, true, noAssert)
};

Buffer$1.prototype.writeFloatBE = function writeFloatBE (value, offset, noAssert) {
    return writeFloat(this, value, offset, false, noAssert)
};

function writeDouble (buf, value, offset, littleEndian, noAssert) {
    if (!noAssert) {
        checkIEEE754(buf, value, offset, 8, 1.7976931348623157E+308, -1.7976931348623157E+308);
    }
    write(buf, value, offset, littleEndian, 52, 8);
    return offset + 8
}

Buffer$1.prototype.writeDoubleLE = function writeDoubleLE (value, offset, noAssert) {
    return writeDouble(this, value, offset, true, noAssert)
};

Buffer$1.prototype.writeDoubleBE = function writeDoubleBE (value, offset, noAssert) {
    return writeDouble(this, value, offset, false, noAssert)
};

// copy(targetBuffer, targetStart=0, sourceStart=0, sourceEnd=buffer.length)
Buffer$1.prototype.copy = function copy (target, targetStart, start, end) {
    var this$1 = this;

    if (!start) { start = 0; }
    if (!end && end !== 0) { end = this.length; }
    if (targetStart >= target.length) { targetStart = target.length; }
    if (!targetStart) { targetStart = 0; }
    if (end > 0 && end < start) { end = start; }

    // Copy 0 bytes; we're done
    if (end === start) { return 0 }
    if (target.length === 0 || this.length === 0) { return 0 }

    // Fatal error conditions
    if (targetStart < 0) {
        throw new RangeError('targetStart out of bounds')
    }
    if (start < 0 || start >= this.length) { throw new RangeError('sourceStart out of bounds') }
    if (end < 0) { throw new RangeError('sourceEnd out of bounds') }

    // Are we oob?
    if (end > this.length) { end = this.length; }
    if (target.length - targetStart < end - start) {
        end = target.length - targetStart + start;
    }

    var len = end - start;
    var i;

    if (this === target && start < targetStart && targetStart < end) {
        // descending copy from end
        for (i = len - 1; i >= 0; --i) {
            target[i + targetStart] = this$1[i + start];
        }
    } else if (len < 1000 || !Buffer$1.TYPED_ARRAY_SUPPORT) {
        // ascending copy from start
        for (i = 0; i < len; ++i) {
            target[i + targetStart] = this$1[i + start];
        }
    } else {
        Uint8Array.prototype.set.call(
            target,
            this.subarray(start, start + len),
            targetStart
        );
    }

    return len
};

// Usage:
//    buffer.fill(number[, offset[, end]])
//    buffer.fill(buffer[, offset[, end]])
//    buffer.fill(string[, offset[, end]][, encoding])
Buffer$1.prototype.fill = function fill (val, start, end, encoding) {
    var this$1 = this;

    // Handle string cases:
    if (typeof val === 'string') {
        if (typeof start === 'string') {
            encoding = start;
            start = 0;
            end = this.length;
        } else if (typeof end === 'string') {
            encoding = end;
            end = this.length;
        }
        if (val.length === 1) {
            var code = val.charCodeAt(0);
            if (code < 256) {
                val = code;
            }
        }
        if (encoding !== undefined && typeof encoding !== 'string') {
            throw new TypeError('encoding must be a string')
        }
        if (typeof encoding === 'string' && !Buffer$1.isEncoding(encoding)) {
            throw new TypeError('Unknown encoding: ' + encoding)
        }
    } else if (typeof val === 'number') {
        val = val & 255;
    }

    // Invalid ranges are not set to a default, so can range check early.
    if (start < 0 || this.length < start || this.length < end) {
        throw new RangeError('Out of range index')
    }

    if (end <= start) {
        return this
    }

    start = start >>> 0;
    end = end === undefined ? this.length : end >>> 0;

    if (!val) { val = 0; }

    var i;
    if (typeof val === 'number') {
        for (i = start; i < end; ++i) {
            this$1[i] = val;
        }
    } else {
        var bytes = internalIsBuffer(val)
            ? val
            : utf8ToBytes(new Buffer$1(val, encoding).toString());
        var len = bytes.length;
        for (i = 0; i < end - start; ++i) {
            this$1[i + start] = bytes[i % len];
        }
    }

    return this
};

// HELPER FUNCTIONS
// ================

var INVALID_BASE64_RE = /[^+\/0-9A-Za-z-_]/g;

function base64clean (str) {
    // Node strips out invalid characters like \n and \t from the string, base64-js does not
    str = stringtrim(str).replace(INVALID_BASE64_RE, '');
    // Node converts strings with length < 2 to ''
    if (str.length < 2) { return '' }
    // Node allows for non-padded base64 strings (missing trailing ===), base64-js does not
    while (str.length % 4 !== 0) {
        str = str + '=';
    }
    return str
}

function stringtrim (str) {
    if (str.trim) { return str.trim() }
    return str.replace(/^\s+|\s+$/g, '')
}

function toHex (n) {
    if (n < 16) { return '0' + n.toString(16) }
    return n.toString(16)
}

function utf8ToBytes (string, units) {
    units = units || Infinity;
    var codePoint;
    var length = string.length;
    var leadSurrogate = null;
    var bytes = [];

    for (var i = 0; i < length; ++i) {
        codePoint = string.charCodeAt(i);

        // is surrogate component
        if (codePoint > 0xD7FF && codePoint < 0xE000) {
            // last char was a lead
            if (!leadSurrogate) {
                // no lead yet
                if (codePoint > 0xDBFF) {
                    // unexpected trail
                    if ((units -= 3) > -1) { bytes.push(0xEF, 0xBF, 0xBD); }
                    continue
                } else if (i + 1 === length) {
                    // unpaired lead
                    if ((units -= 3) > -1) { bytes.push(0xEF, 0xBF, 0xBD); }
                    continue
                }

                // valid lead
                leadSurrogate = codePoint;

                continue
            }

            // 2 leads in a row
            if (codePoint < 0xDC00) {
                if ((units -= 3) > -1) { bytes.push(0xEF, 0xBF, 0xBD); }
                leadSurrogate = codePoint;
                continue
            }

            // valid surrogate pair
            codePoint = (leadSurrogate - 0xD800 << 10 | codePoint - 0xDC00) + 0x10000;
        } else if (leadSurrogate) {
            // valid bmp char, but last char was a lead
            if ((units -= 3) > -1) { bytes.push(0xEF, 0xBF, 0xBD); }
        }

        leadSurrogate = null;

        // encode utf8
        if (codePoint < 0x80) {
            if ((units -= 1) < 0) { break }
            bytes.push(codePoint);
        } else if (codePoint < 0x800) {
            if ((units -= 2) < 0) { break }
            bytes.push(
                codePoint >> 0x6 | 0xC0,
                codePoint & 0x3F | 0x80
            );
        } else if (codePoint < 0x10000) {
            if ((units -= 3) < 0) { break }
            bytes.push(
                codePoint >> 0xC | 0xE0,
                codePoint >> 0x6 & 0x3F | 0x80,
                codePoint & 0x3F | 0x80
            );
        } else if (codePoint < 0x110000) {
            if ((units -= 4) < 0) { break }
            bytes.push(
                codePoint >> 0x12 | 0xF0,
                codePoint >> 0xC & 0x3F | 0x80,
                codePoint >> 0x6 & 0x3F | 0x80,
                codePoint & 0x3F | 0x80
            );
        } else {
            throw new Error('Invalid code point')
        }
    }

    return bytes
}

function asciiToBytes (str) {
    var byteArray = [];
    for (var i = 0; i < str.length; ++i) {
        // Node's code seems to be doing this and not & 0x7F..
        byteArray.push(str.charCodeAt(i) & 0xFF);
    }
    return byteArray
}

function utf16leToBytes (str, units) {
    var c, hi, lo;
    var byteArray = [];
    for (var i = 0; i < str.length; ++i) {
        if ((units -= 2) < 0) { break }

        c = str.charCodeAt(i);
        hi = c >> 8;
        lo = c % 256;
        byteArray.push(lo);
        byteArray.push(hi);
    }

    return byteArray
}


function base64ToBytes (str) {
    return toByteArray(base64clean(str))
}

function blitBuffer (src, dst, offset, length) {
    for (var i = 0; i < length; ++i) {
        if ((i + offset >= dst.length) || (i >= src.length)) { break }
        dst[i + offset] = src[i];
    }
    return i
}

function isnan (val) {
    return val !== val // eslint-disable-line no-self-compare
}


// the following is from is-buffer, also by Feross Aboukhadijeh and with same lisence
// The _isBuffer check is for Safari 5-7 support, because it's missing
// Object.prototype.constructor. Remove this eventually
function isBuffer$1(obj) {
    return obj != null && (!!obj._isBuffer || isFastBuffer(obj) || isSlowBuffer(obj))
}

function isFastBuffer (obj) {
    return !!obj.constructor && typeof obj.constructor.isBuffer === 'function' && obj.constructor.isBuffer(obj)
}

// For Node v0.10 support. Remove this eventually.
function isSlowBuffer (obj) {
    return typeof obj.readFloatLE === 'function' && typeof obj.slice === 'function' && isFastBuffer(obj.slice(0, 0))
}

function BufferList$1() {
    this.head = null;
    this.tail = null;
    this.length = 0;
}

BufferList$1.prototype.push = function (v) {
    var entry = { data: v, next: null };
    if (this.length > 0) { this.tail.next = entry; }else { this.head = entry; }
    this.tail = entry;
    ++this.length;
};

BufferList$1.prototype.unshift = function (v) {
    var entry = { data: v, next: this.head };
    if (this.length === 0) { this.tail = entry; }
    this.head = entry;
    ++this.length;
};

BufferList$1.prototype.shift = function () {
    if (this.length === 0) { return; }
    var ret = this.head.data;
    if (this.length === 1) { this.head = this.tail = null; }else { this.head = this.head.next; }
    --this.length;
    return ret;
};

BufferList$1.prototype.clear = function () {
    this.head = this.tail = null;
    this.length = 0;
};

BufferList$1.prototype.join = function (s) {
    if (this.length === 0) { return ''; }
    var p = this.head;
    var ret = '' + p.data;
    while (p = p.next) {
        ret += s + p.data;
    }return ret;
};

BufferList$1.prototype.concat = function (n) {
    if (this.length === 0) { return Buffer$1.alloc(0); }
    if (this.length === 1) { return this.head.data; }
    var ret = Buffer$1.allocUnsafe(n >>> 0);
    var p = this.head;
    var i = 0;
    while (p) {
        p.data.copy(ret, i);
        i += p.data.length;
        p = p.next;
    }
    return ret;
};

// Copyright Joyent, Inc. and other Node contributors.
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the
// following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
// USE OR OTHER DEALINGS IN THE SOFTWARE.

var isBufferEncoding = Buffer$1.isEncoding
    || function(encoding) {
        switch (encoding && encoding.toLowerCase()) {
            case 'hex': case 'utf8': case 'utf-8': case 'ascii': case 'binary': case 'base64': case 'ucs2': case 'ucs-2': case 'utf16le': case 'utf-16le': case 'raw': return true;
            default: return false;
        }
    };


function assertEncoding(encoding) {
    if (encoding && !isBufferEncoding(encoding)) {
        throw new Error('Unknown encoding: ' + encoding);
    }
}

// StringDecoder provides an interface for efficiently splitting a series of
// buffers into a series of JS strings without breaking apart multi-byte
// characters. CESU-8 is handled as part of the UTF-8 encoding.
//
// @TODO Handling all encodings inside a single object makes it very difficult
// to reason about this code, so it should be split up in the future.
// @TODO There should be a utf8-strict encoding that rejects invalid UTF-8 code
// points as used by CESU-8.
function StringDecoder(encoding) {
    this.encoding = (encoding || 'utf8').toLowerCase().replace(/[-_]/, '');
    assertEncoding(encoding);
    switch (this.encoding) {
        case 'utf8':
            // CESU-8 represents each of Surrogate Pair by 3-bytes
            this.surrogateSize = 3;
            break;
        case 'ucs2':
        case 'utf16le':
            // UTF-16 represents each of Surrogate Pair by 2-bytes
            this.surrogateSize = 2;
            this.detectIncompleteChar = utf16DetectIncompleteChar;
            break;
        case 'base64':
            // Base-64 stores 3 bytes in 4 chars, and pads the remainder.
            this.surrogateSize = 3;
            this.detectIncompleteChar = base64DetectIncompleteChar;
            break;
        default:
            this.write = passThroughWrite;
            return;
    }

    // Enough space to store all bytes of a single character. UTF-8 needs 4
    // bytes, but CESU-8 may require up to 6 (3 bytes per surrogate).
    this.charBuffer = new Buffer$1(6);
    // Number of bytes received for the current incomplete multi-byte character.
    this.charReceived = 0;
    // Number of bytes expected for the current incomplete multi-byte character.
    this.charLength = 0;
}


// write decodes the given buffer and returns it as JS string that is
// guaranteed to not contain any partial multi-byte characters. Any partial
// character found at the end of the buffer is buffered up, and will be
// returned when calling write again with the remaining bytes.
//
// Note: Converting a Buffer containing an orphan surrogate to a String
// currently works, but converting a String to a Buffer (via `new Buffer`, or
// Buffer#write) will replace incomplete surrogates with the unicode
// replacement character. See https://codereview.chromium.org/121173009/ .
StringDecoder.prototype.write = function(buffer) {
    var this$1 = this;

    var charStr = '';
    // if our last write ended with an incomplete multibyte character
    while (this.charLength) {
        // determine how many remaining bytes this buffer has to offer for this char
        var available = (buffer.length >= this$1.charLength - this$1.charReceived) ?
            this$1.charLength - this$1.charReceived :
            buffer.length;

        // add the new bytes to the char buffer
        buffer.copy(this$1.charBuffer, this$1.charReceived, 0, available);
        this$1.charReceived += available;

        if (this$1.charReceived < this$1.charLength) {
            // still not enough chars in this buffer? wait for more ...
            return '';
        }

        // remove bytes belonging to the current character from the buffer
        buffer = buffer.slice(available, buffer.length);

        // get the character that was split
        charStr = this$1.charBuffer.slice(0, this$1.charLength).toString(this$1.encoding);

        // CESU-8: lead surrogate (D800-DBFF) is also the incomplete character
        var charCode = charStr.charCodeAt(charStr.length - 1);
        if (charCode >= 0xD800 && charCode <= 0xDBFF) {
            this$1.charLength += this$1.surrogateSize;
            charStr = '';
            continue;
        }
        this$1.charReceived = this$1.charLength = 0;

        // if there are no more bytes in this buffer, just emit our char
        if (buffer.length === 0) {
            return charStr;
        }
        break;
    }

    // determine and set charLength / charReceived
    this.detectIncompleteChar(buffer);

    var end = buffer.length;
    if (this.charLength) {
        // buffer the incomplete character bytes we got
        buffer.copy(this.charBuffer, 0, buffer.length - this.charReceived, end);
        end -= this.charReceived;
    }

    charStr += buffer.toString(this.encoding, 0, end);

    var end = charStr.length - 1;
    var charCode = charStr.charCodeAt(end);
    // CESU-8: lead surrogate (D800-DBFF) is also the incomplete character
    if (charCode >= 0xD800 && charCode <= 0xDBFF) {
        var size = this.surrogateSize;
        this.charLength += size;
        this.charReceived += size;
        this.charBuffer.copy(this.charBuffer, size, 0, size);
        buffer.copy(this.charBuffer, 0, 0, size);
        return charStr.substring(0, end);
    }

    // or just emit the charStr
    return charStr;
};

// detectIncompleteChar determines if there is an incomplete UTF-8 character at
// the end of the given buffer. If so, it sets this.charLength to the byte
// length that character, and sets this.charReceived to the number of bytes
// that are available for this character.
StringDecoder.prototype.detectIncompleteChar = function(buffer) {
    var this$1 = this;

    // determine how many bytes we have to check at the end of this buffer
    var i = (buffer.length >= 3) ? 3 : buffer.length;

    // Figure out if one of the last i bytes of our buffer announces an
    // incomplete char.
    for (; i > 0; i--) {
        var c = buffer[buffer.length - i];

        // See http://en.wikipedia.org/wiki/UTF-8#Description

        // 110XXXXX
        if (i == 1 && c >> 5 == 0x06) {
            this$1.charLength = 2;
            break;
        }

        // 1110XXXX
        if (i <= 2 && c >> 4 == 0x0E) {
            this$1.charLength = 3;
            break;
        }

        // 11110XXX
        if (i <= 3 && c >> 3 == 0x1E) {
            this$1.charLength = 4;
            break;
        }
    }
    this.charReceived = i;
};

StringDecoder.prototype.end = function(buffer) {
    var res = '';
    if (buffer && buffer.length)
    { res = this.write(buffer); }

    if (this.charReceived) {
        var cr = this.charReceived;
        var buf = this.charBuffer;
        var enc = this.encoding;
        res += buf.slice(0, cr).toString(enc);
    }

    return res;
};

function passThroughWrite(buffer) {
    return buffer.toString(this.encoding);
}

function utf16DetectIncompleteChar(buffer) {
    this.charReceived = buffer.length % 2;
    this.charLength = this.charReceived ? 2 : 0;
}

function base64DetectIncompleteChar(buffer) {
    this.charReceived = buffer.length % 3;
    this.charLength = this.charReceived ? 3 : 0;
}

Readable$1.ReadableState = ReadableState;
var debug = debuglog('stream');
inherits$1(Readable$1, EventEmitter);

function prependListener(emitter, event, fn) {
    // Sadly this is not cacheable as some libraries bundle their own
    // event emitter implementation with them.
    if (typeof emitter.prependListener === 'function') {
        return emitter.prependListener(event, fn);
    } else {
        // This is a hack to make sure that our error handler is attached before any
        // userland ones.  NEVER DO THIS. This is here only because this code needs
        // to continue to work with older versions of Node.js that do not include
        // the prependListener() method. The goal is to eventually remove this hack.
        if (!emitter._events || !emitter._events[event])
        { emitter.on(event, fn); }
        else if (Array.isArray(emitter._events[event]))
        { emitter._events[event].unshift(fn); }
        else
        { emitter._events[event] = [fn, emitter._events[event]]; }
    }
}
function listenerCount$1 (emitter, type) {
    return emitter.listeners(type).length;
}
function ReadableState(options, stream) {

    options = options || {};

    // object stream flag. Used to make read(n) ignore n and to
    // make all the buffer merging and length checks go away
    this.objectMode = !!options.objectMode;

    if (stream instanceof Duplex$1) { this.objectMode = this.objectMode || !!options.readableObjectMode; }

    // the point at which it stops calling _read() to fill the buffer
    // Note: 0 is a valid value, means "don't call _read preemptively ever"
    var hwm = options.highWaterMark;
    var defaultHwm = this.objectMode ? 16 : 16 * 1024;
    this.highWaterMark = hwm || hwm === 0 ? hwm : defaultHwm;

    // cast to ints.
    this.highWaterMark = ~ ~this.highWaterMark;

    // A linked list is used to store data chunks instead of an array because the
    // linked list can remove elements from the beginning faster than
    // array.shift()
    this.buffer = new BufferList$1();
    this.length = 0;
    this.pipes = null;
    this.pipesCount = 0;
    this.flowing = null;
    this.ended = false;
    this.endEmitted = false;
    this.reading = false;

    // a flag to be able to tell if the onwrite cb is called immediately,
    // or on a later tick.  We set this to true at first, because any
    // actions that shouldn't happen until "later" should generally also
    // not happen before the first write call.
    this.sync = true;

    // whenever we return null, then we set a flag to say
    // that we're awaiting a 'readable' event emission.
    this.needReadable = false;
    this.emittedReadable = false;
    this.readableListening = false;
    this.resumeScheduled = false;

    // Crypto is kind of old and crusty.  Historically, its default string
    // encoding is 'binary' so we have to make this configurable.
    // Everything else in the universe uses 'utf8', though.
    this.defaultEncoding = options.defaultEncoding || 'utf8';

    // when piping, we only care about 'readable' events that happen
    // after read()ing all the bytes and not getting any pushback.
    this.ranOut = false;

    // the number of writers that are awaiting a drain event in .pipe()s
    this.awaitDrain = 0;

    // if true, a maybeReadMore has been scheduled
    this.readingMore = false;

    this.decoder = null;
    this.encoding = null;
    if (options.encoding) {
        this.decoder = new StringDecoder(options.encoding);
        this.encoding = options.encoding;
    }
}
function Readable$1(options) {

    if (!(this instanceof Readable$1)) { return new Readable$1(options); }

    this._readableState = new ReadableState(options, this);

    // legacy
    this.readable = true;

    if (options && typeof options.read === 'function') { this._read = options.read; }

    EventEmitter.call(this);
}

// Manually shove something into the read() buffer.
// This returns true if the highWaterMark has not been hit yet,
// similar to how Writable.write() returns true if you should
// write() some more.
Readable$1.prototype.push = function (chunk, encoding) {
    var state = this._readableState;

    if (!state.objectMode && typeof chunk === 'string') {
        encoding = encoding || state.defaultEncoding;
        if (encoding !== state.encoding) {
            chunk = Buffer$1.from(chunk, encoding);
            encoding = '';
        }
    }

    return readableAddChunk(this, state, chunk, encoding, false);
};

// Unshift should *always* be something directly out of read()
Readable$1.prototype.unshift = function (chunk) {
    var state = this._readableState;
    return readableAddChunk(this, state, chunk, '', true);
};

Readable$1.prototype.isPaused = function () {
    return this._readableState.flowing === false;
};

function readableAddChunk(stream, state, chunk, encoding, addToFront) {
    var er = chunkInvalid(state, chunk);
    if (er) {
        stream.emit('error', er);
    } else if (chunk === null) {
        state.reading = false;
        onEofChunk(stream, state);
    } else if (state.objectMode || chunk && chunk.length > 0) {
        if (state.ended && !addToFront) {
            var e = new Error('stream.push() after EOF');
            stream.emit('error', e);
        } else if (state.endEmitted && addToFront) {
            var _e = new Error('stream.unshift() after end event');
            stream.emit('error', _e);
        } else {
            var skipAdd;
            if (state.decoder && !addToFront && !encoding) {
                chunk = state.decoder.write(chunk);
                skipAdd = !state.objectMode && chunk.length === 0;
            }

            if (!addToFront) { state.reading = false; }

            // Don't add to the buffer if we've decoded to an empty string chunk and
            // we're not in object mode
            if (!skipAdd) {
                // if we want the data now, just emit it.
                if (state.flowing && state.length === 0 && !state.sync) {
                    stream.emit('data', chunk);
                    stream.read(0);
                } else {
                    // update the buffer info.
                    state.length += state.objectMode ? 1 : chunk.length;
                    if (addToFront) { state.buffer.unshift(chunk); }else { state.buffer.push(chunk); }

                    if (state.needReadable) { emitReadable(stream); }
                }
            }

            maybeReadMore(stream, state);
        }
    } else if (!addToFront) {
        state.reading = false;
    }

    return needMoreData(state);
}

// if it's past the high water mark, we can push in some more.
// Also, if we have no data yet, we can stand some
// more bytes.  This is to work around cases where hwm=0,
// such as the repl.  Also, if the push() triggered a
// readable event, and the user called read(largeNumber) such that
// needReadable was set, then we ought to push more, so that another
// 'readable' event will be triggered.
function needMoreData(state) {
    return !state.ended && (state.needReadable || state.length < state.highWaterMark || state.length === 0);
}

// backwards compatibility.
Readable$1.prototype.setEncoding = function (enc) {
    this._readableState.decoder = new StringDecoder(enc);
    this._readableState.encoding = enc;
    return this;
};

// Don't raise the hwm > 8MB
var MAX_HWM = 0x800000;
function computeNewHighWaterMark(n) {
    if (n >= MAX_HWM) {
        n = MAX_HWM;
    } else {
        // Get the next highest power of 2 to prevent increasing hwm excessively in
        // tiny amounts
        n--;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        n++;
    }
    return n;
}

// This function is designed to be inlinable, so please take care when making
// changes to the function body.
function howMuchToRead(n, state) {
    if (n <= 0 || state.length === 0 && state.ended) { return 0; }
    if (state.objectMode) { return 1; }
    if (n !== n) {
        // Only flow one buffer at a time
        if (state.flowing && state.length) { return state.buffer.head.data.length; }else { return state.length; }
    }
    // If we're asking for more than the current hwm, then raise the hwm.
    if (n > state.highWaterMark) { state.highWaterMark = computeNewHighWaterMark(n); }
    if (n <= state.length) { return n; }
    // Don't have enough
    if (!state.ended) {
        state.needReadable = true;
        return 0;
    }
    return state.length;
}

// you can override either this method, or the async _read(n) below.
Readable$1.prototype.read = function (n) {
    debug('read', n);
    n = parseInt(n, 10);
    var state = this._readableState;
    var nOrig = n;

    if (n !== 0) { state.emittedReadable = false; }

    // if we're doing read(0) to trigger a readable event, but we
    // already have a bunch of data in the buffer, then just trigger
    // the 'readable' event and move on.
    if (n === 0 && state.needReadable && (state.length >= state.highWaterMark || state.ended)) {
        debug('read: emitReadable', state.length, state.ended);
        if (state.length === 0 && state.ended) { endReadable(this); }else { emitReadable(this); }
        return null;
    }

    n = howMuchToRead(n, state);

    // if we've ended, and we're now clear, then finish it up.
    if (n === 0 && state.ended) {
        if (state.length === 0) { endReadable(this); }
        return null;
    }

    // All the actual chunk generation logic needs to be
    // *below* the call to _read.  The reason is that in certain
    // synthetic stream cases, such as passthrough streams, _read
    // may be a completely synchronous operation which may change
    // the state of the read buffer, providing enough data when
    // before there was *not* enough.
    //
    // So, the steps are:
    // 1. Figure out what the state of things will be after we do
    // a read from the buffer.
    //
    // 2. If that resulting state will trigger a _read, then call _read.
    // Note that this may be asynchronous, or synchronous.  Yes, it is
    // deeply ugly to write APIs this way, but that still doesn't mean
    // that the Readable class should behave improperly, as streams are
    // designed to be sync/async agnostic.
    // Take note if the _read call is sync or async (ie, if the read call
    // has returned yet), so that we know whether or not it's safe to emit
    // 'readable' etc.
    //
    // 3. Actually pull the requested chunks out of the buffer and return.

    // if we need a readable event, then we need to do some reading.
    var doRead = state.needReadable;
    debug('need readable', doRead);

    // if we currently have less than the highWaterMark, then also read some
    if (state.length === 0 || state.length - n < state.highWaterMark) {
        doRead = true;
        debug('length less than watermark', doRead);
    }

    // however, if we've ended, then there's no point, and if we're already
    // reading, then it's unnecessary.
    if (state.ended || state.reading) {
        doRead = false;
        debug('reading or ended', doRead);
    } else if (doRead) {
        debug('do read');
        state.reading = true;
        state.sync = true;
        // if the length is currently zero, then we *need* a readable event.
        if (state.length === 0) { state.needReadable = true; }
        // call internal read method
        this._read(state.highWaterMark);
        state.sync = false;
        // If _read pushed data synchronously, then `reading` will be false,
        // and we need to re-evaluate how much data we can return to the user.
        if (!state.reading) { n = howMuchToRead(nOrig, state); }
    }

    var ret;
    if (n > 0) { ret = fromList(n, state); }else { ret = null; }

    if (ret === null) {
        state.needReadable = true;
        n = 0;
    } else {
        state.length -= n;
    }

    if (state.length === 0) {
        // If we have nothing in the buffer, then we want to know
        // as soon as we *do* get something into the buffer.
        if (!state.ended) { state.needReadable = true; }

        // If we tried to read() past the EOF, then emit end on the next tick.
        if (nOrig !== n && state.ended) { endReadable(this); }
    }

    if (ret !== null) { this.emit('data', ret); }

    return ret;
};

function chunkInvalid(state, chunk) {
    var er = null;
    if (!Buffer$1.isBuffer(chunk) && typeof chunk !== 'string' && chunk !== null && chunk !== undefined && !state.objectMode) {
        er = new TypeError('Invalid non-string/buffer chunk');
    }
    return er;
}

function onEofChunk(stream, state) {
    if (state.ended) { return; }
    if (state.decoder) {
        var chunk = state.decoder.end();
        if (chunk && chunk.length) {
            state.buffer.push(chunk);
            state.length += state.objectMode ? 1 : chunk.length;
        }
    }
    state.ended = true;

    // emit 'readable' now to make sure it gets picked up.
    emitReadable(stream);
}

// Don't emit readable right away in sync mode, because this can trigger
// another read() call => stack overflow.  This way, it might trigger
// a nextTick recursion warning, but that's not so bad.
function emitReadable(stream) {
    var state = stream._readableState;
    state.needReadable = false;
    if (!state.emittedReadable) {
        debug('emitReadable', state.flowing);
        state.emittedReadable = true;
        if (state.sync) { nextTick(emitReadable_, stream); }else { emitReadable_(stream); }
    }
}

function emitReadable_(stream) {
    debug('emit readable');
    stream.emit('readable');
    flow(stream);
}

// at this point, the user has presumably seen the 'readable' event,
// and called read() to consume some data.  that may have triggered
// in turn another _read(n) call, in which case reading = true if
// it's in progress.
// However, if we're not ended, or reading, and the length < hwm,
// then go ahead and try to read some more preemptively.
function maybeReadMore(stream, state) {
    if (!state.readingMore) {
        state.readingMore = true;
        nextTick(maybeReadMore_, stream, state);
    }
}

function maybeReadMore_(stream, state) {
    var len = state.length;
    while (!state.reading && !state.flowing && !state.ended && state.length < state.highWaterMark) {
        debug('maybeReadMore read 0');
        stream.read(0);
        if (len === state.length)
            // didn't get any data, stop spinning.
        { break; }else { len = state.length; }
    }
    state.readingMore = false;
}

// abstract method.  to be overridden in specific implementation classes.
// call cb(er, data) where data is <= n in length.
// for virtual (non-string, non-buffer) streams, "length" is somewhat
// arbitrary, and perhaps not very meaningful.
Readable$1.prototype._read = function (n) {
    this.emit('error', new Error('not implemented'));
};

Readable$1.prototype.pipe = function (dest, pipeOpts) {
    var src = this;
    var state = this._readableState;

    switch (state.pipesCount) {
        case 0:
            state.pipes = dest;
            break;
        case 1:
            state.pipes = [state.pipes, dest];
            break;
        default:
            state.pipes.push(dest);
            break;
    }
    state.pipesCount += 1;
    debug('pipe count=%d opts=%j', state.pipesCount, pipeOpts);

    var doEnd = (!pipeOpts || pipeOpts.end !== false);

    var endFn = doEnd ? onend : cleanup;
    if (state.endEmitted) { nextTick(endFn); }else { src.once('end', endFn); }

    dest.on('unpipe', onunpipe);
    function onunpipe(readable) {
        debug('onunpipe');
        if (readable === src) {
            cleanup();
        }
    }

    function onend() {
        debug('onend');
        dest.end();
    }

    // when the dest drains, it reduces the awaitDrain counter
    // on the source.  This would be more elegant with a .once()
    // handler in flow(), but adding and removing repeatedly is
    // too slow.
    var ondrain = pipeOnDrain(src);
    dest.on('drain', ondrain);

    var cleanedUp = false;
    function cleanup() {
        debug('cleanup');
        // cleanup event handlers once the pipe is broken
        dest.removeListener('close', onclose);
        dest.removeListener('finish', onfinish);
        dest.removeListener('drain', ondrain);
        dest.removeListener('error', onerror);
        dest.removeListener('unpipe', onunpipe);
        src.removeListener('end', onend);
        src.removeListener('end', cleanup);
        src.removeListener('data', ondata);

        cleanedUp = true;

        // if the reader is waiting for a drain event from this
        // specific writer, then it would cause it to never start
        // flowing again.
        // So, if this is awaiting a drain, then we just call it now.
        // If we don't know, then assume that we are waiting for one.
        if (state.awaitDrain && (!dest._writableState || dest._writableState.needDrain)) { ondrain(); }
    }

    // If the user pushes more data while we're writing to dest then we'll end up
    // in ondata again. However, we only want to increase awaitDrain once because
    // dest will only emit one 'drain' event for the multiple writes.
    // => Introduce a guard on increasing awaitDrain.
    var increasedAwaitDrain = false;
    src.on('data', ondata);
    function ondata(chunk) {
        debug('ondata');
        increasedAwaitDrain = false;
        var ret = dest.write(chunk);
        if (false === ret && !increasedAwaitDrain) {
            // If the user unpiped during `dest.write()`, it is possible
            // to get stuck in a permanently paused state if that write
            // also returned false.
            // => Check whether `dest` is still a piping destination.
            if ((state.pipesCount === 1 && state.pipes === dest || state.pipesCount > 1 && indexOf(state.pipes, dest) !== -1) && !cleanedUp) {
                debug('false write response, pause', src._readableState.awaitDrain);
                src._readableState.awaitDrain++;
                increasedAwaitDrain = true;
            }
            src.pause();
        }
    }

    // if the dest has an error, then stop piping into it.
    // however, don't suppress the throwing behavior for this.
    function onerror(er) {
        debug('onerror', er);
        unpipe();
        dest.removeListener('error', onerror);
        if (listenerCount$1(dest, 'error') === 0) { dest.emit('error', er); }
    }

    // Make sure our error handler is attached before userland ones.
    prependListener(dest, 'error', onerror);

    // Both close and finish should trigger unpipe, but only once.
    function onclose() {
        dest.removeListener('finish', onfinish);
        unpipe();
    }
    dest.once('close', onclose);
    function onfinish() {
        debug('onfinish');
        dest.removeListener('close', onclose);
        unpipe();
    }
    dest.once('finish', onfinish);

    function unpipe() {
        debug('unpipe');
        src.unpipe(dest);
    }

    // tell the dest that it's being piped to
    dest.emit('pipe', src);

    // start the flow if it hasn't been started already.
    if (!state.flowing) {
        debug('pipe resume');
        src.resume();
    }

    return dest;
};

function pipeOnDrain(src) {
    return function () {
        var state = src._readableState;
        debug('pipeOnDrain', state.awaitDrain);
        if (state.awaitDrain) { state.awaitDrain--; }
        if (state.awaitDrain === 0 && src.listeners('data').length) {
            state.flowing = true;
            flow(src);
        }
    };
}

Readable$1.prototype.unpipe = function (dest) {
    var this$1 = this;

    var state = this._readableState;

    // if we're not piping anywhere, then do nothing.
    if (state.pipesCount === 0) { return this; }

    // just one destination.  most common case.
    if (state.pipesCount === 1) {
        // passed in one, but it's not the right one.
        if (dest && dest !== state.pipes) { return this; }

        if (!dest) { dest = state.pipes; }

        // got a match.
        state.pipes = null;
        state.pipesCount = 0;
        state.flowing = false;
        if (dest) { dest.emit('unpipe', this); }
        return this;
    }

    // slow case. multiple pipe destinations.

    if (!dest) {
        // remove all.
        var dests = state.pipes;
        var len = state.pipesCount;
        state.pipes = null;
        state.pipesCount = 0;
        state.flowing = false;

        for (var _i = 0; _i < len; _i++) {
            dests[_i].emit('unpipe', this$1);
        }return this;
    }

    // try to find the right one.
    var i = indexOf(state.pipes, dest);
    if (i === -1) { return this; }

    state.pipes.splice(i, 1);
    state.pipesCount -= 1;
    if (state.pipesCount === 1) { state.pipes = state.pipes[0]; }

    dest.emit('unpipe', this);

    return this;
};

// set up data events if they are asked for
// Ensure readable listeners eventually get something
Readable$1.prototype.on = function (ev, fn) {
    var res = EventEmitter.prototype.on.call(this, ev, fn);

    if (ev === 'data') {
        // Start flowing on next tick if stream isn't explicitly paused
        if (this._readableState.flowing !== false) { this.resume(); }
    } else if (ev === 'readable') {
        var state = this._readableState;
        if (!state.endEmitted && !state.readableListening) {
            state.readableListening = state.needReadable = true;
            state.emittedReadable = false;
            if (!state.reading) {
                nextTick(nReadingNextTick, this);
            } else if (state.length) {
                emitReadable(this, state);
            }
        }
    }

    return res;
};
Readable$1.prototype.addListener = Readable$1.prototype.on;

function nReadingNextTick(self) {
    debug('readable nexttick read 0');
    self.read(0);
}

// pause() and resume() are remnants of the legacy readable stream API
// If the user uses them, then switch into old mode.
Readable$1.prototype.resume = function () {
    var state = this._readableState;
    if (!state.flowing) {
        debug('resume');
        state.flowing = true;
        resume(this, state);
    }
    return this;
};

function resume(stream, state) {
    if (!state.resumeScheduled) {
        state.resumeScheduled = true;
        nextTick(resume_, stream, state);
    }
}

function resume_(stream, state) {
    if (!state.reading) {
        debug('resume read 0');
        stream.read(0);
    }

    state.resumeScheduled = false;
    state.awaitDrain = 0;
    stream.emit('resume');
    flow(stream);
    if (state.flowing && !state.reading) { stream.read(0); }
}

Readable$1.prototype.pause = function () {
    debug('call pause flowing=%j', this._readableState.flowing);
    if (false !== this._readableState.flowing) {
        debug('pause');
        this._readableState.flowing = false;
        this.emit('pause');
    }
    return this;
};

function flow(stream) {
    var state = stream._readableState;
    debug('flow', state.flowing);
    while (state.flowing && stream.read() !== null) {}
}

// wrap an old-style stream as the async data source.
// This is *not* part of the readable stream interface.
// It is an ugly unfortunate mess of history.
Readable$1.prototype.wrap = function (stream) {
    var this$1 = this;

    var state = this._readableState;
    var paused = false;

    var self = this;
    stream.on('end', function () {
        debug('wrapped end');
        if (state.decoder && !state.ended) {
            var chunk = state.decoder.end();
            if (chunk && chunk.length) { self.push(chunk); }
        }

        self.push(null);
    });

    stream.on('data', function (chunk) {
        debug('wrapped data');
        if (state.decoder) { chunk = state.decoder.write(chunk); }

        // don't skip over falsy values in objectMode
        if (state.objectMode && (chunk === null || chunk === undefined)) { return; }else if (!state.objectMode && (!chunk || !chunk.length)) { return; }

        var ret = self.push(chunk);
        if (!ret) {
            paused = true;
            stream.pause();
        }
    });

    // proxy all the other methods.
    // important when wrapping filters and duplexes.
    for (var i in stream) {
        if (this$1[i] === undefined && typeof stream[i] === 'function') {
            this$1[i] = function (method) {
                return function () {
                    return stream[method].apply(stream, arguments);
                };
            }(i);
        }
    }

    // proxy certain important events.
    var events = ['error', 'close', 'destroy', 'pause', 'resume'];
    forEach(events, function (ev) {
        stream.on(ev, self.emit.bind(self, ev));
    });

    // when we try to consume some more bytes, simply unpause the
    // underlying stream.
    self._read = function (n) {
        debug('wrapped _read', n);
        if (paused) {
            paused = false;
            stream.resume();
        }
    };

    return self;
};

// exposed for testing purposes only.
Readable$1._fromList = fromList;

// Pluck off n bytes from an array of buffers.
// Length is the combined lengths of all the buffers in the list.
// This function is designed to be inlinable, so please take care when making
// changes to the function body.
function fromList(n, state) {
    // nothing buffered
    if (state.length === 0) { return null; }

    var ret;
    if (state.objectMode) { ret = state.buffer.shift(); }else if (!n || n >= state.length) {
        // read it all, truncate the list
        if (state.decoder) { ret = state.buffer.join(''); }else if (state.buffer.length === 1) { ret = state.buffer.head.data; }else { ret = state.buffer.concat(state.length); }
        state.buffer.clear();
    } else {
        // read part of list
        ret = fromListPartial(n, state.buffer, state.decoder);
    }

    return ret;
}

// Extracts only enough buffered data to satisfy the amount requested.
// This function is designed to be inlinable, so please take care when making
// changes to the function body.
function fromListPartial(n, list, hasStrings) {
    var ret;
    if (n < list.head.data.length) {
        // slice is the same for buffers and strings
        ret = list.head.data.slice(0, n);
        list.head.data = list.head.data.slice(n);
    } else if (n === list.head.data.length) {
        // first chunk is a perfect match
        ret = list.shift();
    } else {
        // result spans more than one buffer
        ret = hasStrings ? copyFromBufferString(n, list) : copyFromBuffer(n, list);
    }
    return ret;
}

// Copies a specified amount of characters from the list of buffered data
// chunks.
// This function is designed to be inlinable, so please take care when making
// changes to the function body.
function copyFromBufferString(n, list) {
    var p = list.head;
    var c = 1;
    var ret = p.data;
    n -= ret.length;
    while (p = p.next) {
        var str = p.data;
        var nb = n > str.length ? str.length : n;
        if (nb === str.length) { ret += str; }else { ret += str.slice(0, n); }
        n -= nb;
        if (n === 0) {
            if (nb === str.length) {
                ++c;
                if (p.next) { list.head = p.next; }else { list.head = list.tail = null; }
            } else {
                list.head = p;
                p.data = str.slice(nb);
            }
            break;
        }
        ++c;
    }
    list.length -= c;
    return ret;
}

// Copies a specified amount of bytes from the list of buffered data chunks.
// This function is designed to be inlinable, so please take care when making
// changes to the function body.
function copyFromBuffer(n, list) {
    var ret = Buffer$1.allocUnsafe(n);
    var p = list.head;
    var c = 1;
    p.data.copy(ret);
    n -= p.data.length;
    while (p = p.next) {
        var buf = p.data;
        var nb = n > buf.length ? buf.length : n;
        buf.copy(ret, ret.length - n, 0, nb);
        n -= nb;
        if (n === 0) {
            if (nb === buf.length) {
                ++c;
                if (p.next) { list.head = p.next; }else { list.head = list.tail = null; }
            } else {
                list.head = p;
                p.data = buf.slice(nb);
            }
            break;
        }
        ++c;
    }
    list.length -= c;
    return ret;
}

function endReadable(stream) {
    var state = stream._readableState;

    // If we get here before consuming all the bytes, then that is a
    // bug in node.  Should never happen.
    if (state.length > 0) { throw new Error('"endReadable()" called on non-empty stream'); }

    if (!state.endEmitted) {
        state.ended = true;
        nextTick(endReadableNT, state, stream);
    }
}

function endReadableNT(state, stream) {
    // Check that we didn't get one last unshift.
    if (!state.endEmitted && state.length === 0) {
        state.endEmitted = true;
        stream.readable = false;
        stream.emit('end');
    }
}

function forEach(xs, f) {
    for (var i = 0, l = xs.length; i < l; i++) {
        f(xs[i], i);
    }
}

function indexOf(xs, x) {
    for (var i = 0, l = xs.length; i < l; i++) {
        if (xs[i] === x) { return i; }
    }
    return -1;
}

// A bit simpler than readable streams.
// Implement an async ._write(chunk, encoding, cb), and it'll handle all
// the drain event emission and buffering.


Writable$1.WritableState = WritableState;
inherits$1(Writable$1, EventEmitter);

function nop() {}

function WriteReq(chunk, encoding, cb) {
    this.chunk = chunk;
    this.encoding = encoding;
    this.callback = cb;
    this.next = null;
}

function WritableState(options, stream) {
    Object.defineProperty(this, 'buffer', {
        get: deprecate(function () {
            return this.getBuffer();
        }, '_writableState.buffer is deprecated. Use _writableState.getBuffer ' + 'instead.')
    });
    options = options || {};

    // object stream flag to indicate whether or not this stream
    // contains buffers or objects.
    this.objectMode = !!options.objectMode;

    if (stream instanceof Duplex$1) { this.objectMode = this.objectMode || !!options.writableObjectMode; }

    // the point at which write() starts returning false
    // Note: 0 is a valid value, means that we always return false if
    // the entire buffer is not flushed immediately on write()
    var hwm = options.highWaterMark;
    var defaultHwm = this.objectMode ? 16 : 16 * 1024;
    this.highWaterMark = hwm || hwm === 0 ? hwm : defaultHwm;

    // cast to ints.
    this.highWaterMark = ~ ~this.highWaterMark;

    this.needDrain = false;
    // at the start of calling end()
    this.ending = false;
    // when end() has been called, and returned
    this.ended = false;
    // when 'finish' is emitted
    this.finished = false;

    // should we decode strings into buffers before passing to _write?
    // this is here so that some node-core streams can optimize string
    // handling at a lower level.
    var noDecode = options.decodeStrings === false;
    this.decodeStrings = !noDecode;

    // Crypto is kind of old and crusty.  Historically, its default string
    // encoding is 'binary' so we have to make this configurable.
    // Everything else in the universe uses 'utf8', though.
    this.defaultEncoding = options.defaultEncoding || 'utf8';

    // not an actual buffer we keep track of, but a measurement
    // of how much we're waiting to get pushed to some underlying
    // socket or file.
    this.length = 0;

    // a flag to see when we're in the middle of a write.
    this.writing = false;

    // when true all writes will be buffered until .uncork() call
    this.corked = 0;

    // a flag to be able to tell if the onwrite cb is called immediately,
    // or on a later tick.  We set this to true at first, because any
    // actions that shouldn't happen until "later" should generally also
    // not happen before the first write call.
    this.sync = true;

    // a flag to know if we're processing previously buffered items, which
    // may call the _write() callback in the same tick, so that we don't
    // end up in an overlapped onwrite situation.
    this.bufferProcessing = false;

    // the callback that's passed to _write(chunk,cb)
    this.onwrite = function (er) {
        onwrite(stream, er);
    };

    // the callback that the user supplies to write(chunk,encoding,cb)
    this.writecb = null;

    // the amount that is being written when _write is called.
    this.writelen = 0;

    this.bufferedRequest = null;
    this.lastBufferedRequest = null;

    // number of pending user-supplied write callbacks
    // this must be 0 before 'finish' can be emitted
    this.pendingcb = 0;

    // emit prefinish if the only thing we're waiting for is _write cbs
    // This is relevant for synchronous Transform streams
    this.prefinished = false;

    // True if the error was already emitted and should not be thrown again
    this.errorEmitted = false;

    // count buffered requests
    this.bufferedRequestCount = 0;

    // allocate the first CorkedRequest, there is always
    // one allocated and free to use, and we maintain at most two
    this.corkedRequestsFree = new CorkedRequest(this);
}

WritableState.prototype.getBuffer = function writableStateGetBuffer() {
    var current = this.bufferedRequest;
    var out = [];
    while (current) {
        out.push(current);
        current = current.next;
    }
    return out;
};

function Writable$1(options) {

    // Writable ctor is applied to Duplexes, though they're not
    // instanceof Writable, they're instanceof Readable.
    if (!(this instanceof Writable$1) && !(this instanceof Duplex$1)) { return new Writable$1(options); }

    this._writableState = new WritableState(options, this);

    // legacy.
    this.writable = true;

    if (options) {
        if (typeof options.write === 'function') { this._write = options.write; }

        if (typeof options.writev === 'function') { this._writev = options.writev; }
    }

    EventEmitter.call(this);
}

// Otherwise people can pipe Writable streams, which is just wrong.
Writable$1.prototype.pipe = function () {
    this.emit('error', new Error('Cannot pipe, not readable'));
};

function writeAfterEnd(stream, cb) {
    var er = new Error('write after end');
    // TODO: defer error events consistently everywhere, not just the cb
    stream.emit('error', er);
    nextTick(cb, er);
}

// If we get something that is not a buffer, string, null, or undefined,
// and we're not in objectMode, then that's an error.
// Otherwise stream chunks are all considered to be of length=1, and the
// watermarks determine how many objects to keep in the buffer, rather than
// how many bytes or characters.
function validChunk(stream, state, chunk, cb) {
    var valid = true;
    var er = false;
    // Always throw error if a null is written
    // if we are not in object mode then throw
    // if it is not a buffer, string, or undefined.
    if (chunk === null) {
        er = new TypeError('May not write null values to stream');
    } else if (!Buffer$1.isBuffer(chunk) && typeof chunk !== 'string' && chunk !== undefined && !state.objectMode) {
        er = new TypeError('Invalid non-string/buffer chunk');
    }
    if (er) {
        stream.emit('error', er);
        nextTick(cb, er);
        valid = false;
    }
    return valid;
}

Writable$1.prototype.write = function (chunk, encoding, cb) {
    var state = this._writableState;
    var ret = false;

    if (typeof encoding === 'function') {
        cb = encoding;
        encoding = null;
    }

    if (Buffer$1.isBuffer(chunk)) { encoding = 'buffer'; }else if (!encoding) { encoding = state.defaultEncoding; }

    if (typeof cb !== 'function') { cb = nop; }

    if (state.ended) { writeAfterEnd(this, cb); }else if (validChunk(this, state, chunk, cb)) {
        state.pendingcb++;
        ret = writeOrBuffer(this, state, chunk, encoding, cb);
    }

    return ret;
};

Writable$1.prototype.cork = function () {
    var state = this._writableState;

    state.corked++;
};

Writable$1.prototype.uncork = function () {
    var state = this._writableState;

    if (state.corked) {
        state.corked--;

        if (!state.writing && !state.corked && !state.finished && !state.bufferProcessing && state.bufferedRequest) { clearBuffer(this, state); }
    }
};

Writable$1.prototype.setDefaultEncoding = function setDefaultEncoding(encoding) {
    // node::ParseEncoding() requires lower case.
    if (typeof encoding === 'string') { encoding = encoding.toLowerCase(); }
    if (!(['hex', 'utf8', 'utf-8', 'ascii', 'binary', 'base64', 'ucs2', 'ucs-2', 'utf16le', 'utf-16le', 'raw'].indexOf((encoding + '').toLowerCase()) > -1)) { throw new TypeError('Unknown encoding: ' + encoding); }
    this._writableState.defaultEncoding = encoding;
    return this;
};

function decodeChunk(state, chunk, encoding) {
    if (!state.objectMode && state.decodeStrings !== false && typeof chunk === 'string') {
        chunk = Buffer$1.from(chunk, encoding);
    }
    return chunk;
}

// if we're already writing something, then just put this
// in the queue, and wait our turn.  Otherwise, call _write
// If we return false, then we need a drain event, so set that flag.
function writeOrBuffer(stream, state, chunk, encoding, cb) {
    chunk = decodeChunk(state, chunk, encoding);

    if (Buffer$1.isBuffer(chunk)) { encoding = 'buffer'; }
    var len = state.objectMode ? 1 : chunk.length;

    state.length += len;

    var ret = state.length < state.highWaterMark;
    // we must ensure that previous needDrain will not be reset to false.
    if (!ret) { state.needDrain = true; }

    if (state.writing || state.corked) {
        var last = state.lastBufferedRequest;
        state.lastBufferedRequest = new WriteReq(chunk, encoding, cb);
        if (last) {
            last.next = state.lastBufferedRequest;
        } else {
            state.bufferedRequest = state.lastBufferedRequest;
        }
        state.bufferedRequestCount += 1;
    } else {
        doWrite(stream, state, false, len, chunk, encoding, cb);
    }

    return ret;
}

function doWrite(stream, state, writev, len, chunk, encoding, cb) {
    state.writelen = len;
    state.writecb = cb;
    state.writing = true;
    state.sync = true;
    if (writev) { stream._writev(chunk, state.onwrite); }else { stream._write(chunk, encoding, state.onwrite); }
    state.sync = false;
}

function onwriteError(stream, state, sync, er, cb) {
    --state.pendingcb;
    if (sync) { nextTick(cb, er); }else { cb(er); }

    stream._writableState.errorEmitted = true;
    stream.emit('error', er);
}

function onwriteStateUpdate(state) {
    state.writing = false;
    state.writecb = null;
    state.length -= state.writelen;
    state.writelen = 0;
}

function onwrite(stream, er) {
    var state = stream._writableState;
    var sync = state.sync;
    var cb = state.writecb;

    onwriteStateUpdate(state);

    if (er) { onwriteError(stream, state, sync, er, cb); }else {
        // Check if we're actually ready to finish, but don't emit yet
        var finished = needFinish(state);

        if (!finished && !state.corked && !state.bufferProcessing && state.bufferedRequest) {
            clearBuffer(stream, state);
        }

        if (sync) {
            /*<replacement>*/
            nextTick(afterWrite, stream, state, finished, cb);
            /*</replacement>*/
        } else {
            afterWrite(stream, state, finished, cb);
        }
    }
}

function afterWrite(stream, state, finished, cb) {
    if (!finished) { onwriteDrain(stream, state); }
    state.pendingcb--;
    cb();
    finishMaybe(stream, state);
}

// Must force callback to be called on nextTick, so that we don't
// emit 'drain' before the write() consumer gets the 'false' return
// value, and has a chance to attach a 'drain' listener.
function onwriteDrain(stream, state) {
    if (state.length === 0 && state.needDrain) {
        state.needDrain = false;
        stream.emit('drain');
    }
}

// if there's something in the buffer waiting, then process it
function clearBuffer(stream, state) {
    state.bufferProcessing = true;
    var entry = state.bufferedRequest;

    if (stream._writev && entry && entry.next) {
        // Fast case, write everything using _writev()
        var l = state.bufferedRequestCount;
        var buffer = new Array(l);
        var holder = state.corkedRequestsFree;
        holder.entry = entry;

        var count = 0;
        while (entry) {
            buffer[count] = entry;
            entry = entry.next;
            count += 1;
        }

        doWrite(stream, state, true, state.length, buffer, '', holder.finish);

        // doWrite is almost always async, defer these to save a bit of time
        // as the hot path ends with doWrite
        state.pendingcb++;
        state.lastBufferedRequest = null;
        if (holder.next) {
            state.corkedRequestsFree = holder.next;
            holder.next = null;
        } else {
            state.corkedRequestsFree = new CorkedRequest(state);
        }
    } else {
        // Slow case, write chunks one-by-one
        while (entry) {
            var chunk = entry.chunk;
            var encoding = entry.encoding;
            var cb = entry.callback;
            var len = state.objectMode ? 1 : chunk.length;

            doWrite(stream, state, false, len, chunk, encoding, cb);
            entry = entry.next;
            // if we didn't call the onwrite immediately, then
            // it means that we need to wait until it does.
            // also, that means that the chunk and cb are currently
            // being processed, so move the buffer counter past them.
            if (state.writing) {
                break;
            }
        }

        if (entry === null) { state.lastBufferedRequest = null; }
    }

    state.bufferedRequestCount = 0;
    state.bufferedRequest = entry;
    state.bufferProcessing = false;
}

Writable$1.prototype._write = function (chunk, encoding, cb) {
    cb(new Error('not implemented'));
};

Writable$1.prototype._writev = null;

Writable$1.prototype.end = function (chunk, encoding, cb) {
    var state = this._writableState;

    if (typeof chunk === 'function') {
        cb = chunk;
        chunk = null;
        encoding = null;
    } else if (typeof encoding === 'function') {
        cb = encoding;
        encoding = null;
    }

    if (chunk !== null && chunk !== undefined) { this.write(chunk, encoding); }

    // .end() fully uncorks
    if (state.corked) {
        state.corked = 1;
        this.uncork();
    }

    // ignore unnecessary end() calls.
    if (!state.ending && !state.finished) { endWritable(this, state, cb); }
};

function needFinish(state) {
    return state.ending && state.length === 0 && state.bufferedRequest === null && !state.finished && !state.writing;
}

function prefinish(stream, state) {
    if (!state.prefinished) {
        state.prefinished = true;
        stream.emit('prefinish');
    }
}

function finishMaybe(stream, state) {
    var need = needFinish(state);
    if (need) {
        if (state.pendingcb === 0) {
            prefinish(stream, state);
            state.finished = true;
            stream.emit('finish');
        } else {
            prefinish(stream, state);
        }
    }
    return need;
}

function endWritable(stream, state, cb) {
    state.ending = true;
    finishMaybe(stream, state);
    if (cb) {
        if (state.finished) { nextTick(cb); }else { stream.once('finish', cb); }
    }
    state.ended = true;
    stream.writable = false;
}

// It seems a linked list but it is not
// there will be only 2 of these for each stream
function CorkedRequest(state) {
    var _this = this;

    this.next = null;
    this.entry = null;

    this.finish = function (err) {
        var entry = _this.entry;
        _this.entry = null;
        while (entry) {
            var cb = entry.callback;
            state.pendingcb--;
            cb(err);
            entry = entry.next;
        }
        if (state.corkedRequestsFree) {
            state.corkedRequestsFree.next = _this;
        } else {
            state.corkedRequestsFree = _this;
        }
    };
}

inherits$1(Duplex$1, Readable$1);

var keys = Object.keys(Writable$1.prototype);
for (var v = 0; v < keys.length; v++) {
    var method = keys[v];
    if (!Duplex$1.prototype[method]) { Duplex$1.prototype[method] = Writable$1.prototype[method]; }
}
function Duplex$1(options) {
    if (!(this instanceof Duplex$1)) { return new Duplex$1(options); }

    Readable$1.call(this, options);
    Writable$1.call(this, options);

    if (options && options.readable === false) { this.readable = false; }

    if (options && options.writable === false) { this.writable = false; }

    this.allowHalfOpen = true;
    if (options && options.allowHalfOpen === false) { this.allowHalfOpen = false; }

    this.once('end', onend);
}

// the no-half-open enforcer
function onend() {
    // if we allow half-open state, or if the writable side ended,
    // then we're ok.
    if (this.allowHalfOpen || this._writableState.ended) { return; }

    // no more data can be written.
    // But allow more writes to happen in this tick.
    nextTick(onEndNT, this);
}

function onEndNT(self) {
    self.end();
}

// a transform stream is a readable/writable stream where you do
// something with the data.  Sometimes it's called a "filter",
// but that's not a great name for it, since that implies a thing where
// some bits pass through, and others are simply ignored.  (That would
// be a valid example of a transform, of course.)
//
// While the output is causally related to the input, it's not a
// necessarily symmetric or synchronous transformation.  For example,
// a zlib stream might take multiple plain-text writes(), and then
// emit a single compressed chunk some time in the future.
//
// Here's how this works:
//
// The Transform stream has all the aspects of the readable and writable
// stream classes.  When you write(chunk), that calls _write(chunk,cb)
// internally, and returns false if there's a lot of pending writes
// buffered up.  When you call read(), that calls _read(n) until
// there's enough pending readable data buffered up.
//
// In a transform stream, the written data is placed in a buffer.  When
// _read(n) is called, it transforms the queued up data, calling the
// buffered _write cb's as it consumes chunks.  If consuming a single
// written chunk would result in multiple output chunks, then the first
// outputted bit calls the readcb, and subsequent chunks just go into
// the read buffer, and will cause it to emit 'readable' if necessary.
//
// This way, back-pressure is actually determined by the reading side,
// since _read has to be called to start processing a new chunk.  However,
// a pathological inflate type of transform can cause excessive buffering
// here.  For example, imagine a stream where every byte of input is
// interpreted as an integer from 0-255, and then results in that many
// bytes of output.  Writing the 4 bytes {ff,ff,ff,ff} would result in
// 1kb of data being output.  In this case, you could write a very small
// amount of input, and end up with a very large amount of output.  In
// such a pathological inflating mechanism, there'd be no way to tell
// the system to stop doing the transform.  A single 4MB write could
// cause the system to run out of memory.
//
// However, even in such a pathological case, only a single written chunk
// would be consumed, and then the rest would wait (un-transformed) until
// the results of the previous transformed chunk were consumed.


inherits$1(Transform$1, Duplex$1);

function TransformState(stream) {
    this.afterTransform = function (er, data) {
        return afterTransform(stream, er, data);
    };

    this.needTransform = false;
    this.transforming = false;
    this.writecb = null;
    this.writechunk = null;
    this.writeencoding = null;
}

function afterTransform(stream, er, data) {
    var ts = stream._transformState;
    ts.transforming = false;

    var cb = ts.writecb;

    if (!cb) { return stream.emit('error', new Error('no writecb in Transform class')); }

    ts.writechunk = null;
    ts.writecb = null;

    if (data !== null && data !== undefined) { stream.push(data); }

    cb(er);

    var rs = stream._readableState;
    rs.reading = false;
    if (rs.needReadable || rs.length < rs.highWaterMark) {
        stream._read(rs.highWaterMark);
    }
}
function Transform$1(options) {
    if (!(this instanceof Transform$1)) { return new Transform$1(options); }

    Duplex$1.call(this, options);

    this._transformState = new TransformState(this);

    // when the writable side finishes, then flush out anything remaining.
    var stream = this;

    // start out asking for a readable event once data is transformed.
    this._readableState.needReadable = true;

    // we have implemented the _read method, and done the other things
    // that Readable wants before the first _read call, so unset the
    // sync guard flag.
    this._readableState.sync = false;

    if (options) {
        if (typeof options.transform === 'function') { this._transform = options.transform; }

        if (typeof options.flush === 'function') { this._flush = options.flush; }
    }

    this.once('prefinish', function () {
        if (typeof this._flush === 'function') { this._flush(function (er) {
            done(stream, er);
        }); }else { done(stream); }
    });
}

Transform$1.prototype.push = function (chunk, encoding) {
    this._transformState.needTransform = false;
    return Duplex$1.prototype.push.call(this, chunk, encoding);
};

// This is the part where you do stuff!
// override this function in implementation classes.
// 'chunk' is an input chunk.
//
// Call `push(newChunk)` to pass along transformed output
// to the readable side.  You may call 'push' zero or more times.
//
// Call `cb(err)` when you are done with this chunk.  If you pass
// an error, then that'll put the hurt on the whole operation.  If you
// never call cb(), then you'll never get another chunk.
Transform$1.prototype._transform = function (chunk, encoding, cb) {
    throw new Error('Not implemented');
};

Transform$1.prototype._write = function (chunk, encoding, cb) {
    var ts = this._transformState;
    ts.writecb = cb;
    ts.writechunk = chunk;
    ts.writeencoding = encoding;
    if (!ts.transforming) {
        var rs = this._readableState;
        if (ts.needTransform || rs.needReadable || rs.length < rs.highWaterMark) { this._read(rs.highWaterMark); }
    }
};

// Doesn't matter what the args are here.
// _transform does all the work.
// That we got here means that the readable side wants more data.
Transform$1.prototype._read = function (n) {
    var ts = this._transformState;

    if (ts.writechunk !== null && ts.writecb && !ts.transforming) {
        ts.transforming = true;
        this._transform(ts.writechunk, ts.writeencoding, ts.afterTransform);
    } else {
        // mark that we need a transform, so that any data that comes in
        // will get processed, now that we've asked for it.
        ts.needTransform = true;
    }
};

function done(stream, er) {
    if (er) { return stream.emit('error', er); }

    // if there's nothing in the write buffer, then that means
    // that nothing more will ever be provided
    var ws = stream._writableState;
    var ts = stream._transformState;

    if (ws.length) { throw new Error('Calling transform done when ws.length != 0'); }

    if (ts.transforming) { throw new Error('Calling transform done when still transforming'); }

    return stream.push(null);
}

inherits$1(PassThrough$1, Transform$1);
function PassThrough$1(options) {
    if (!(this instanceof PassThrough$1)) { return new PassThrough$1(options); }

    Transform$1.call(this, options);
}

PassThrough$1.prototype._transform = function (chunk, encoding, cb) {
    cb(null, chunk);
};

inherits$1(Stream$1, EventEmitter);
Stream$1.Readable = Readable$1;
Stream$1.Writable = Writable$1;
Stream$1.Duplex = Duplex$1;
Stream$1.Transform = Transform$1;
Stream$1.PassThrough = PassThrough$1;

// Backwards-compat with node 0.4.x
Stream$1.Stream = Stream$1;

// old-style streams.  Note that the pipe method (the only relevant
// part of this class) is overridden in the Readable class.

function Stream$1() {
    EventEmitter.call(this);
}

Stream$1.prototype.pipe = function(dest, options) {
    var source = this;

    function ondata(chunk) {
        if (dest.writable) {
            if (false === dest.write(chunk) && source.pause) {
                source.pause();
            }
        }
    }

    source.on('data', ondata);

    function ondrain() {
        if (source.readable && source.resume) {
            source.resume();
        }
    }

    dest.on('drain', ondrain);

    // If the 'end' option is not supplied, dest.end() will be called when
    // source gets the 'end' or 'close' events.  Only dest.end() once.
    if (!dest._isStdio && (!options || options.end !== false)) {
        source.on('end', onend);
        source.on('close', onclose);
    }

    var didOnEnd = false;
    function onend() {
        if (didOnEnd) { return; }
        didOnEnd = true;

        dest.end();
    }


    function onclose() {
        if (didOnEnd) { return; }
        didOnEnd = true;

        if (typeof dest.destroy === 'function') { dest.destroy(); }
    }

    // don't leave dangling pipes when there are errors.
    function onerror(er) {
        cleanup();
        if (EventEmitter.listenerCount(this, 'error') === 0) {
            throw er; // Unhandled stream error in pipe.
        }
    }

    source.on('error', onerror);
    dest.on('error', onerror);

    // remove all the event listeners that were added.
    function cleanup() {
        source.removeListener('data', ondata);
        dest.removeListener('drain', ondrain);

        source.removeListener('end', onend);
        source.removeListener('close', onclose);

        source.removeListener('error', onerror);
        dest.removeListener('error', onerror);

        source.removeListener('end', cleanup);
        source.removeListener('close', cleanup);

        dest.removeListener('close', cleanup);
    }

    source.on('end', cleanup);
    source.on('close', cleanup);

    dest.on('close', cleanup);

    dest.emit('pipe', source);

    // Allow for unix-like usage: A.pipe(B).pipe(C)
    return dest;
};

/*  */

var MAX_STACK_DEPTH = 190 || 1000;

function createWriteFunction (
    write,
    onError
) {
    var stackDepth = 0;
    var cachedWrite = function (text, next) {
        if (text && cachedWrite.caching) {
            cachedWrite.cacheBuffer[cachedWrite.cacheBuffer.length - 1] += text;
        }
        var waitForNext = write(text, next);
        if (waitForNext !== true) {
            if (stackDepth >= MAX_STACK_DEPTH) {
                process.nextTick(function () {
                    try { next(); } catch (e) {
                        onError(e);
                    }
                });
            } else {
                stackDepth++;
                next();
                stackDepth--;
            }
        }
    };
    cachedWrite.caching = false;
    cachedWrite.cacheBuffer = [];
    cachedWrite.componentBuffer = [];
    return cachedWrite
}

/*  */

/**
 * Original RenderStream implementation by Sasha Aickin (@aickin)
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modified by Evan You (@yyx990803)
 */

//const stream = require('stream')
var RenderStream = (function (Readable$$1) {
    function RenderStream (render) {
        var this$1 = this;

        Readable$$1.call(this);
        this.buffer = '';
        this.render = render;
        this.expectedSize = 0;

        this.write = createWriteFunction(function (text, next) {
            var n = this$1.expectedSize;
            this$1.buffer += text;
            if (this$1.buffer.length >= n) {
                this$1.next = next;
                this$1.pushBySize(n);
                return true // we will decide when to call next
            }
            return false
        }, function (err) {
            this$1.emit('error', err);
        });

        this.end = function () {
            // the rendering is finished; we should push out the last of the buffer.
            this$1.done = true;
            this$1.push(this$1.buffer);
        };
    }

    if ( Readable$$1 ) RenderStream.__proto__ = Readable$$1;
    RenderStream.prototype = Object.create( Readable$$1 && Readable$$1.prototype );
    RenderStream.prototype.constructor = RenderStream;

    RenderStream.prototype.pushBySize = function pushBySize (n) {
        var bufferToPush = this.buffer.substring(0, n);
        this.buffer = this.buffer.substring(n);
        this.push(bufferToPush);
    };

    RenderStream.prototype.tryRender = function tryRender () {
        try {
            this.render(this.write, this.end);
        } catch (e) {
            this.emit('error', e);
        }
    };

    RenderStream.prototype.tryNext = function tryNext () {
        try {
            this.next();
        } catch (e) {
            this.emit('error', e);
        }
    };

    RenderStream.prototype._read = function _read (n) {
        this.expectedSize = n;
        // it's possible that the last chunk added bumped the buffer up to > 2 * n,
        // which means we will need to go through multiple read calls to drain it
        // down to < n.
        if (isTrue(this.done)) {
            this.push(null);
            return
        }
        if (this.buffer.length >= n) {
            this.pushBySize(n);
            return
        }
        if (isUndef(this.next)) {
            // start the rendering chain.
            this.tryRender();
        } else {
            // continue with the rendering.
            this.tryNext();
        }
    };

    return RenderStream;
}(Readable$1));

/*  */

function extname (path) {
    console.log('path.extname', path);
    return path
}







// export { filesystem };
// export { filesystem.extname }

var require$$0 = ( util$1 && util ) || util$1;

var isRegExp$2 = require$$0.isRegExp;

// Generate an internal UID to make the regexp pattern harder to guess.
var UID                 = Math.floor(Math.random() * 0x10000000000).toString(16);
var PLACE_HOLDER_REGEXP = new RegExp('"@__(F|R)-' + UID + '-(\\d+)__@"', 'g');

var IS_NATIVE_CODE_REGEXP = /\{\s*\[native code\]\s*\}/g;
var UNSAFE_CHARS_REGEXP   = /[<>\/\u2028\u2029]/g;

// Mapping of unsafe HTML and invalid JavaScript line terminator chars to their
// Unicode char counterparts which are safe to use in JavaScript strings.
var ESCAPED_CHARS = {
    '<'     : '\\u003C',
    '>'     : '\\u003E',
    '/'     : '\\u002F',
    '\u2028': '\\u2028',
    '\u2029': '\\u2029'
};

function escapeUnsafeChars(unsafeChar) {
    return ESCAPED_CHARS[unsafeChar];
}

var index$2 = function serialize(obj, options) {
    options || (options = {});

    // Backwards-compatability for `space` as the second argument.
    if (typeof options === 'number' || typeof options === 'string') {
        options = {space: options};
    }

    var functions = [];
    var regexps   = [];

    // Returns placeholders for functions and regexps (identified by index)
    // which are later replaced by their string representation.
    function replacer(key, value) {
        if (!value) {
            return value;
        }

        var type = typeof value;

        if (type === 'object') {
            if (isRegExp$2(value)) {
                return '@__R-' + UID + '-' + (regexps.push(value) - 1) + '__@';
            }

            return value;
        }

        if (type === 'function') {
            return '@__F-' + UID + '-' + (functions.push(value) - 1) + '__@';
        }

        return value;
    }

    var str;

    // Creates a JSON string representation of the value.
    // NOTE: Node 0.12 goes into slow mode with extra JSON.stringify() args.
    if (options.isJSON && !options.space) {
        str = JSON.stringify(obj);
    } else {
        str = JSON.stringify(obj, options.isJSON ? null : replacer, options.space);
    }

    // Protects against `JSON.stringify()` returning `undefined`, by serializing
    // to the literal string: "undefined".
    if (typeof str !== 'string') {
        return String(str);
    }

    // Replace unsafe HTML and invalid JavaScript line terminator chars with
    // their safe Unicode char counterpart. This _must_ happen before the
    // regexps and functions are serialized and added back to the string.
    str = str.replace(UNSAFE_CHARS_REGEXP, escapeUnsafeChars);

    if (functions.length === 0 && regexps.length === 0) {
        return str;
    }

    // Replaces all occurrences of function and regexp placeholders in the JSON
    // string with their string representations. If the original value can not
    // be found, then `undefined` is used.
    return str.replace(PLACE_HOLDER_REGEXP, function (match, type, valueIndex) {
        if (type === 'R') {
            return regexps[valueIndex].toString();
        }

        var fn           = functions[valueIndex];
        var serializedFn = fn.toString();

        if (IS_NATIVE_CODE_REGEXP.test(serializedFn)) {
            throw new TypeError('Serializing native function: ' + fn.name);
        }

        return serializedFn;
    });
};

var index_1 = index$2.serialize;

/*  */

var isJS = function (file) { return /\.js(\?[^.]+)?$/.test(file); };

var isCSS = function (file) { return /\.css(\?[^.]+)?$/.test(file); };

/*  */

//const Transform = require('stream').Transform
var TemplateStream = (function (Transform$$1) {
    function TemplateStream (
        renderer,
        template,
        context
    ) {
        Transform$$1.call(this);
        this.started = false;
        this.renderer = renderer;
        this.template = template;
        this.context = context || {};
        this.inject = renderer.inject;
    }

    if ( Transform$$1 ) TemplateStream.__proto__ = Transform$$1;
    TemplateStream.prototype = Object.create( Transform$$1 && Transform$$1.prototype );
    TemplateStream.prototype.constructor = TemplateStream;

    TemplateStream.prototype._transform = function _transform (data, encoding, done) {
        if (!this.started) {
            this.emit('beforeStart');
            this.start();
        }
        this.push(data);
        done();
    };

    TemplateStream.prototype.start = function start () {
        this.started = true;
        this.push(this.template.head(this.context));

        if (this.inject) {
            // inline server-rendered head meta information
            if (this.context.head) {
                this.push(this.context.head);
            }

            // inline preload/prefetch directives for initial/async chunks
            var links = this.renderer.renderResourceHints(this.context);
            if (links) {
                this.push(links);
            }

            // CSS files and inline server-rendered CSS collected by vue-style-loader
            var styles = this.renderer.renderStyles(this.context);
            if (styles) {
                this.push(styles);
            }
        }

        this.push(this.template.neck(this.context));
    };

    TemplateStream.prototype._flush = function _flush (done) {
        this.emit('beforeEnd');

        if (this.inject) {
            // inline initial store state
            var state = this.renderer.renderState(this.context);
            if (state) {
                this.push(state);
            }

            // embed scripts needed
            var scripts = this.renderer.renderScripts(this.context);
            if (scripts) {
                this.push(scripts);
            }
        }

        this.push(this.template.tail(this.context));
        done();
    };

    return TemplateStream;
}(Transform$1));

/** Detect free variable `global` from Node.js. */
var freeGlobal = typeof commonjsGlobal == 'object' && commonjsGlobal && commonjsGlobal.Object === Object && commonjsGlobal;

var _freeGlobal = freeGlobal;

/** Detect free variable `self`. */
var freeSelf = typeof self == 'object' && self && self.Object === Object && self;

/** Used as a reference to the global object. */
var root = _freeGlobal || freeSelf || Function('return this')();

var _root = root;

/** Built-in value references. */
var Symbol$1 = _root.Symbol;

var _Symbol = Symbol$1;

/** Used for built-in method references. */
var objectProto$2 = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty$4 = objectProto$2.hasOwnProperty;

/**
 * Used to resolve the
 * [`toStringTag`](http://ecma-international.org/ecma-262/7.0/#sec-object.prototype.tostring)
 * of values.
 */
var nativeObjectToString = objectProto$2.toString;

/** Built-in value references. */
var symToStringTag$1 = _Symbol ? _Symbol.toStringTag : undefined;

/**
 * A specialized version of `baseGetTag` which ignores `Symbol.toStringTag` values.
 *
 * @private
 * @param {*} value The value to query.
 * @returns {string} Returns the raw `toStringTag`.
 */
function getRawTag(value) {
    var isOwn = hasOwnProperty$4.call(value, symToStringTag$1),
        tag = value[symToStringTag$1];

    try {
        value[symToStringTag$1] = undefined;
        var unmasked = true;
    } catch (e) {}

    var result = nativeObjectToString.call(value);
    if (unmasked) {
        if (isOwn) {
            value[symToStringTag$1] = tag;
        } else {
            delete value[symToStringTag$1];
        }
    }
    return result;
}

var _getRawTag = getRawTag;

/** Used for built-in method references. */
var objectProto$3 = Object.prototype;

/**
 * Used to resolve the
 * [`toStringTag`](http://ecma-international.org/ecma-262/7.0/#sec-object.prototype.tostring)
 * of values.
 */
var nativeObjectToString$1 = objectProto$3.toString;

/**
 * Converts `value` to a string using `Object.prototype.toString`.
 *
 * @private
 * @param {*} value The value to convert.
 * @returns {string} Returns the converted string.
 */
function objectToString$1(value) {
    return nativeObjectToString$1.call(value);
}

var _objectToString = objectToString$1;

/** `Object#toString` result references. */
var nullTag = '[object Null]';
var undefinedTag = '[object Undefined]';

/** Built-in value references. */
var symToStringTag = _Symbol ? _Symbol.toStringTag : undefined;

/**
 * The base implementation of `getTag` without fallbacks for buggy environments.
 *
 * @private
 * @param {*} value The value to query.
 * @returns {string} Returns the `toStringTag`.
 */
function baseGetTag(value) {
    if (value == null) {
        return value === undefined ? undefinedTag : nullTag;
    }
    return (symToStringTag && symToStringTag in Object(value))
        ? _getRawTag(value)
        : _objectToString(value);
}

var _baseGetTag = baseGetTag;

/**
 * Checks if `value` is the
 * [language type](http://www.ecma-international.org/ecma-262/7.0/#sec-ecmascript-language-types)
 * of `Object`. (e.g. arrays, functions, objects, regexes, `new Number(0)`, and `new String('')`)
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is an object, else `false`.
 * @example
 *
 * _.isObject({});
 * // => true
 *
 * _.isObject([1, 2, 3]);
 * // => true
 *
 * _.isObject(_.noop);
 * // => true
 *
 * _.isObject(null);
 * // => false
 */
function isObject$2(value) {
    var type = typeof value;
    return value != null && (type == 'object' || type == 'function');
}

var isObject_1 = isObject$2;

/** `Object#toString` result references. */
var asyncTag = '[object AsyncFunction]';
var funcTag = '[object Function]';
var genTag = '[object GeneratorFunction]';
var proxyTag = '[object Proxy]';

/**
 * Checks if `value` is classified as a `Function` object.
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a function, else `false`.
 * @example
 *
 * _.isFunction(_);
 * // => true
 *
 * _.isFunction(/abc/);
 * // => false
 */
function isFunction$1(value) {
    if (!isObject_1(value)) {
        return false;
    }
    // The use of `Object#toString` avoids issues with the `typeof` operator
    // in Safari 9 which returns 'object' for typed arrays and other constructors.
    var tag = _baseGetTag(value);
    return tag == funcTag || tag == genTag || tag == asyncTag || tag == proxyTag;
}

var isFunction_1 = isFunction$1;

/** Used to detect overreaching core-js shims. */
var coreJsData = _root['__core-js_shared__'];

var _coreJsData = coreJsData;

/** Used to detect methods masquerading as native. */
var maskSrcKey = (function() {
    var uid = /[^.]+$/.exec(_coreJsData && _coreJsData.keys && _coreJsData.keys.IE_PROTO || '');
    return uid ? ('Symbol(src)_1.' + uid) : '';
}());

/**
 * Checks if `func` has its source masked.
 *
 * @private
 * @param {Function} func The function to check.
 * @returns {boolean} Returns `true` if `func` is masked, else `false`.
 */
function isMasked(func) {
    return !!maskSrcKey && (maskSrcKey in func);
}

var _isMasked = isMasked;

/** Used for built-in method references. */
var funcProto$1 = Function.prototype;

/** Used to resolve the decompiled source of functions. */
var funcToString$1 = funcProto$1.toString;

/**
 * Converts `func` to its source code.
 *
 * @private
 * @param {Function} func The function to convert.
 * @returns {string} Returns the source code.
 */
function toSource(func) {
    if (func != null) {
        try {
            return funcToString$1.call(func);
        } catch (e) {}
        try {
            return (func + '');
        } catch (e) {}
    }
    return '';
}

var _toSource = toSource;

/**
 * Used to match `RegExp`
 * [syntax characters](http://ecma-international.org/ecma-262/7.0/#sec-patterns).
 */
var reRegExpChar = /[\\^$.*+?()[\]{}|]/g;

/** Used to detect host constructors (Safari). */
var reIsHostCtor = /^\[object .+?Constructor\]$/;

/** Used for built-in method references. */
var funcProto = Function.prototype;
var objectProto$1 = Object.prototype;

/** Used to resolve the decompiled source of functions. */
var funcToString = funcProto.toString;

/** Used to check objects for own properties. */
var hasOwnProperty$3 = objectProto$1.hasOwnProperty;

/** Used to detect if a method is native. */
var reIsNative = RegExp('^' +
    funcToString.call(hasOwnProperty$3).replace(reRegExpChar, '\\$&')
        .replace(/hasOwnProperty|(function).*?(?=\\\()| for .+?(?=\\\])/g, '$1.*?') + '$'
);

/**
 * The base implementation of `_.isNative` without bad shim checks.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a native function,
 *  else `false`.
 */
function baseIsNative(value) {
    if (!isObject_1(value) || _isMasked(value)) {
        return false;
    }
    var pattern = isFunction_1(value) ? reIsNative : reIsHostCtor;
    return pattern.test(_toSource(value));
}

var _baseIsNative = baseIsNative;

/**
 * Gets the value at `key` of `object`.
 *
 * @private
 * @param {Object} [object] The object to query.
 * @param {string} key The key of the property to get.
 * @returns {*} Returns the property value.
 */
function getValue$1(object, key) {
    return object == null ? undefined : object[key];
}

var _getValue = getValue$1;

/**
 * Gets the native function at `key` of `object`.
 *
 * @private
 * @param {Object} object The object to query.
 * @param {string} key The key of the method to get.
 * @returns {*} Returns the function if it's native, else `undefined`.
 */
function getNative(object, key) {
    var value = _getValue(object, key);
    return _baseIsNative(value) ? value : undefined;
}

var _getNative = getNative;

var defineProperty = (function() {
    try {
        var func = _getNative(Object, 'defineProperty');
        func({}, '', {});
        return func;
    } catch (e) {}
}());

var _defineProperty = defineProperty;

/**
 * The base implementation of `assignValue` and `assignMergeValue` without
 * value checks.
 *
 * @private
 * @param {Object} object The object to modify.
 * @param {string} key The key of the property to assign.
 * @param {*} value The value to assign.
 */
function baseAssignValue(object, key, value) {
    if (key == '__proto__' && _defineProperty) {
        _defineProperty(object, key, {
            'configurable': true,
            'enumerable': true,
            'value': value,
            'writable': true
        });
    } else {
        object[key] = value;
    }
}

var _baseAssignValue = baseAssignValue;

/**
 * Performs a
 * [`SameValueZero`](http://ecma-international.org/ecma-262/7.0/#sec-samevaluezero)
 * comparison between two values to determine if they are equivalent.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to compare.
 * @param {*} other The other value to compare.
 * @returns {boolean} Returns `true` if the values are equivalent, else `false`.
 * @example
 *
 * var object = { 'a': 1 };
 * var other = { 'a': 1 };
 *
 * _.eq(object, object);
 * // => true
 *
 * _.eq(object, other);
 * // => false
 *
 * _.eq('a', 'a');
 * // => true
 *
 * _.eq('a', Object('a'));
 * // => false
 *
 * _.eq(NaN, NaN);
 * // => true
 */
function eq(value, other) {
    return value === other || (value !== value && other !== other);
}

var eq_1 = eq;

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty$2 = objectProto.hasOwnProperty;

/**
 * Assigns `value` to `key` of `object` if the existing value is not equivalent
 * using [`SameValueZero`](http://ecma-international.org/ecma-262/7.0/#sec-samevaluezero)
 * for equality comparisons.
 *
 * @private
 * @param {Object} object The object to modify.
 * @param {string} key The key of the property to assign.
 * @param {*} value The value to assign.
 */
function assignValue(object, key, value) {
    var objValue = object[key];
    if (!(hasOwnProperty$2.call(object, key) && eq_1(objValue, value)) ||
        (value === undefined && !(key in object))) {
        _baseAssignValue(object, key, value);
    }
}

var _assignValue = assignValue;

/**
 * Copies properties of `source` to `object`.
 *
 * @private
 * @param {Object} source The object to copy properties from.
 * @param {Array} props The property identifiers to copy.
 * @param {Object} [object={}] The object to copy properties to.
 * @param {Function} [customizer] The function to customize copied values.
 * @returns {Object} Returns `object`.
 */
function copyObject(source, props, object, customizer) {
    var isNew = !object;
    object || (object = {});

    var index = -1,
        length = props.length;

    while (++index < length) {
        var key = props[index];

        var newValue = customizer
            ? customizer(object[key], source[key], key, object, source)
            : undefined;

        if (newValue === undefined) {
            newValue = source[key];
        }
        if (isNew) {
            _baseAssignValue(object, key, newValue);
        } else {
            _assignValue(object, key, newValue);
        }
    }
    return object;
}

var _copyObject = copyObject;

/**
 * This method returns the first argument it receives.
 *
 * @static
 * @since 0.1.0
 * @memberOf _
 * @category Util
 * @param {*} value Any value.
 * @returns {*} Returns `value`.
 * @example
 *
 * var object = { 'a': 1 };
 *
 * console.log(_.identity(object) === object);
 * // => true
 */
function identity$1(value) {
    return value;
}

var identity_1 = identity$1;

/**
 * A faster alternative to `Function#apply`, this function invokes `func`
 * with the `this` binding of `thisArg` and the arguments of `args`.
 *
 * @private
 * @param {Function} func The function to invoke.
 * @param {*} thisArg The `this` binding of `func`.
 * @param {Array} args The arguments to invoke `func` with.
 * @returns {*} Returns the result of `func`.
 */
function apply(func, thisArg, args) {
    switch (args.length) {
        case 0: return func.call(thisArg);
        case 1: return func.call(thisArg, args[0]);
        case 2: return func.call(thisArg, args[0], args[1]);
        case 3: return func.call(thisArg, args[0], args[1], args[2]);
    }
    return func.apply(thisArg, args);
}

var _apply = apply;

/* Built-in method references for those with the same name as other `lodash` methods. */
var nativeMax = Math.max;

/**
 * A specialized version of `baseRest` which transforms the rest array.
 *
 * @private
 * @param {Function} func The function to apply a rest parameter to.
 * @param {number} [start=func.length-1] The start position of the rest parameter.
 * @param {Function} transform The rest array transform.
 * @returns {Function} Returns the new function.
 */
function overRest(func, start, transform) {
    start = nativeMax(start === undefined ? (func.length - 1) : start, 0);
    return function() {
        var args = arguments,
            index = -1,
            length = nativeMax(args.length - start, 0),
            array = Array(length);

        while (++index < length) {
            array[index] = args[start + index];
        }
        index = -1;
        var otherArgs = Array(start + 1);
        while (++index < start) {
            otherArgs[index] = args[index];
        }
        otherArgs[start] = transform(array);
        return _apply(func, this, otherArgs);
    };
}

var _overRest = overRest;

/**
 * Creates a function that returns `value`.
 *
 * @static
 * @memberOf _
 * @since 2.4.0
 * @category Util
 * @param {*} value The value to return from the new function.
 * @returns {Function} Returns the new constant function.
 * @example
 *
 * var objects = _.times(2, _.constant({ 'a': 1 }));
 *
 * console.log(objects);
 * // => [{ 'a': 1 }, { 'a': 1 }]
 *
 * console.log(objects[0] === objects[1]);
 * // => true
 */
function constant(value) {
    return function() {
        return value;
    };
}

var constant_1 = constant;

/**
 * The base implementation of `setToString` without support for hot loop shorting.
 *
 * @private
 * @param {Function} func The function to modify.
 * @param {Function} string The `toString` result.
 * @returns {Function} Returns `func`.
 */
var baseSetToString = !_defineProperty ? identity_1 : function(func, string) {
    return _defineProperty(func, 'toString', {
        'configurable': true,
        'enumerable': false,
        'value': constant_1(string),
        'writable': true
    });
};

var _baseSetToString = baseSetToString;

/** Used to detect hot functions by number of calls within a span of milliseconds. */
var HOT_COUNT = 800;
var HOT_SPAN = 16;

/* Built-in method references for those with the same name as other `lodash` methods. */
var nativeNow = Date.now;

/**
 * Creates a function that'll short out and invoke `identity` instead
 * of `func` when it's called `HOT_COUNT` or more times in `HOT_SPAN`
 * milliseconds.
 *
 * @private
 * @param {Function} func The function to restrict.
 * @returns {Function} Returns the new shortable function.
 */
function shortOut(func) {
    var count = 0,
        lastCalled = 0;

    return function() {
        var stamp = nativeNow(),
            remaining = HOT_SPAN - (stamp - lastCalled);

        lastCalled = stamp;
        if (remaining > 0) {
            if (++count >= HOT_COUNT) {
                return arguments[0];
            }
        } else {
            count = 0;
        }
        return func.apply(undefined, arguments);
    };
}

var _shortOut = shortOut;

/**
 * Sets the `toString` method of `func` to return `string`.
 *
 * @private
 * @param {Function} func The function to modify.
 * @param {Function} string The `toString` result.
 * @returns {Function} Returns `func`.
 */
var setToString = _shortOut(_baseSetToString);

var _setToString = setToString;

/**
 * The base implementation of `_.rest` which doesn't validate or coerce arguments.
 *
 * @private
 * @param {Function} func The function to apply a rest parameter to.
 * @param {number} [start=func.length-1] The start position of the rest parameter.
 * @returns {Function} Returns the new function.
 */
function baseRest(func, start) {
    return _setToString(_overRest(func, start, identity_1), func + '');
}

var _baseRest = baseRest;

/** Used as references for various `Number` constants. */
var MAX_SAFE_INTEGER = 9007199254740991;

/**
 * Checks if `value` is a valid array-like length.
 *
 * **Note:** This method is loosely based on
 * [`ToLength`](http://ecma-international.org/ecma-262/7.0/#sec-tolength).
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a valid length, else `false`.
 * @example
 *
 * _.isLength(3);
 * // => true
 *
 * _.isLength(Number.MIN_VALUE);
 * // => false
 *
 * _.isLength(Infinity);
 * // => false
 *
 * _.isLength('3');
 * // => false
 */
function isLength(value) {
    return typeof value == 'number' &&
        value > -1 && value % 1 == 0 && value <= MAX_SAFE_INTEGER;
}

var isLength_1 = isLength;

/**
 * Checks if `value` is array-like. A value is considered array-like if it's
 * not a function and has a `value.length` that's an integer greater than or
 * equal to `0` and less than or equal to `Number.MAX_SAFE_INTEGER`.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is array-like, else `false`.
 * @example
 *
 * _.isArrayLike([1, 2, 3]);
 * // => true
 *
 * _.isArrayLike(document.body.children);
 * // => true
 *
 * _.isArrayLike('abc');
 * // => true
 *
 * _.isArrayLike(_.noop);
 * // => false
 */
function isArrayLike(value) {
    return value != null && isLength_1(value.length) && !isFunction_1(value);
}

var isArrayLike_1 = isArrayLike;

/** Used as references for various `Number` constants. */
var MAX_SAFE_INTEGER$1 = 9007199254740991;

/** Used to detect unsigned integer values. */
var reIsUint = /^(?:0|[1-9]\d*)$/;

/**
 * Checks if `value` is a valid array-like index.
 *
 * @private
 * @param {*} value The value to check.
 * @param {number} [length=MAX_SAFE_INTEGER] The upper bounds of a valid index.
 * @returns {boolean} Returns `true` if `value` is a valid index, else `false`.
 */
function isIndex(value, length) {
    length = length == null ? MAX_SAFE_INTEGER$1 : length;
    return !!length &&
        (typeof value == 'number' || reIsUint.test(value)) &&
        (value > -1 && value % 1 == 0 && value < length);
}

var _isIndex = isIndex;

/**
 * Checks if the given arguments are from an iteratee call.
 *
 * @private
 * @param {*} value The potential iteratee value argument.
 * @param {*} index The potential iteratee index or key argument.
 * @param {*} object The potential iteratee object argument.
 * @returns {boolean} Returns `true` if the arguments are from an iteratee call,
 *  else `false`.
 */
function isIterateeCall(value, index, object) {
    if (!isObject_1(object)) {
        return false;
    }
    var type = typeof index;
    if (type == 'number'
        ? (isArrayLike_1(object) && _isIndex(index, object.length))
        : (type == 'string' && index in object)
    ) {
        return eq_1(object[index], value);
    }
    return false;
}

var _isIterateeCall = isIterateeCall;

/**
 * Creates a function like `_.assign`.
 *
 * @private
 * @param {Function} assigner The function to assign values.
 * @returns {Function} Returns the new assigner function.
 */
function createAssigner(assigner) {
    return _baseRest(function(object, sources) {
        var index = -1,
            length = sources.length,
            customizer = length > 1 ? sources[length - 1] : undefined,
            guard = length > 2 ? sources[2] : undefined;

        customizer = (assigner.length > 3 && typeof customizer == 'function')
            ? (length--, customizer)
            : undefined;

        if (guard && _isIterateeCall(sources[0], sources[1], guard)) {
            customizer = length < 3 ? undefined : customizer;
            length = 1;
        }
        object = Object(object);
        while (++index < length) {
            var source = sources[index];
            if (source) {
                assigner(object, source, index, customizer);
            }
        }
        return object;
    });
}

var _createAssigner = createAssigner;

/**
 * The base implementation of `_.times` without support for iteratee shorthands
 * or max array length checks.
 *
 * @private
 * @param {number} n The number of times to invoke `iteratee`.
 * @param {Function} iteratee The function invoked per iteration.
 * @returns {Array} Returns the array of results.
 */
function baseTimes(n, iteratee) {
    var index = -1,
        result = Array(n);

    while (++index < n) {
        result[index] = iteratee(index);
    }
    return result;
}

var _baseTimes = baseTimes;

/**
 * Checks if `value` is object-like. A value is object-like if it's not `null`
 * and has a `typeof` result of "object".
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is object-like, else `false`.
 * @example
 *
 * _.isObjectLike({});
 * // => true
 *
 * _.isObjectLike([1, 2, 3]);
 * // => true
 *
 * _.isObjectLike(_.noop);
 * // => false
 *
 * _.isObjectLike(null);
 * // => false
 */
function isObjectLike(value) {
    return value != null && typeof value == 'object';
}

var isObjectLike_1 = isObjectLike;

/** `Object#toString` result references. */
var argsTag = '[object Arguments]';

/**
 * The base implementation of `_.isArguments`.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is an `arguments` object,
 */
function baseIsArguments(value) {
    return isObjectLike_1(value) && _baseGetTag(value) == argsTag;
}

var _baseIsArguments = baseIsArguments;

/** Used for built-in method references. */
var objectProto$5 = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty$6 = objectProto$5.hasOwnProperty;

/** Built-in value references. */
var propertyIsEnumerable = objectProto$5.propertyIsEnumerable;

/**
 * Checks if `value` is likely an `arguments` object.
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is an `arguments` object,
 *  else `false`.
 * @example
 *
 * _.isArguments(function() { return arguments; }());
 * // => true
 *
 * _.isArguments([1, 2, 3]);
 * // => false
 */
var isArguments = _baseIsArguments(function() { return arguments; }()) ? _baseIsArguments : function(value) {
    return isObjectLike_1(value) && hasOwnProperty$6.call(value, 'callee') &&
        !propertyIsEnumerable.call(value, 'callee');
};

var isArguments_1 = isArguments;

/**
 * Checks if `value` is classified as an `Array` object.
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is an array, else `false`.
 * @example
 *
 * _.isArray([1, 2, 3]);
 * // => true
 *
 * _.isArray(document.body.children);
 * // => false
 *
 * _.isArray('abc');
 * // => false
 *
 * _.isArray(_.noop);
 * // => false
 */
var isArray$2 = Array.isArray;

var isArray_1 = isArray$2;

/**
 * This method returns `false`.
 *
 * @static
 * @memberOf _
 * @since 4.13.0
 * @category Util
 * @returns {boolean} Returns `false`.
 * @example
 *
 * _.times(2, _.stubFalse);
 * // => [false, false]
 */
function stubFalse() {
    return false;
}

var stubFalse_1 = stubFalse;

var isBuffer_1 = createCommonjsModule(function (module, exports) {
    /** Detect free variable `exports`. */
    var freeExports = 'object' == 'object' && exports && !exports.nodeType && exports;

    /** Detect free variable `module`. */
    var freeModule = freeExports && 'object' == 'object' && module && !module.nodeType && module;

    /** Detect the popular CommonJS extension `module.exports`. */
    var moduleExports = freeModule && freeModule.exports === freeExports;

    /** Built-in value references. */
    var Buffer = moduleExports ? _root.Buffer : undefined;

    /* Built-in method references for those with the same name as other `lodash` methods. */
    var nativeIsBuffer = Buffer ? Buffer.isBuffer : undefined;

    /**
     * Checks if `value` is a buffer.
     *
     * @static
     * @memberOf _
     * @since 4.3.0
     * @category Lang
     * @param {*} value The value to check.
     * @returns {boolean} Returns `true` if `value` is a buffer, else `false`.
     * @example
     *
     * _.isBuffer(new Buffer(2));
     * // => true
     *
     * _.isBuffer(new Uint8Array(2));
     * // => false
     */
    var isBuffer = nativeIsBuffer || stubFalse_1;

    module.exports = isBuffer;
});

/** `Object#toString` result references. */
var argsTag$1 = '[object Arguments]';
var arrayTag = '[object Array]';
var boolTag = '[object Boolean]';
var dateTag = '[object Date]';
var errorTag = '[object Error]';
var funcTag$1 = '[object Function]';
var mapTag = '[object Map]';
var numberTag = '[object Number]';
var objectTag = '[object Object]';
var regexpTag = '[object RegExp]';
var setTag = '[object Set]';
var stringTag = '[object String]';
var weakMapTag = '[object WeakMap]';

var arrayBufferTag = '[object ArrayBuffer]';
var dataViewTag = '[object DataView]';
var float32Tag = '[object Float32Array]';
var float64Tag = '[object Float64Array]';
var int8Tag = '[object Int8Array]';
var int16Tag = '[object Int16Array]';
var int32Tag = '[object Int32Array]';
var uint8Tag = '[object Uint8Array]';
var uint8ClampedTag = '[object Uint8ClampedArray]';
var uint16Tag = '[object Uint16Array]';
var uint32Tag = '[object Uint32Array]';

/** Used to identify `toStringTag` values of typed arrays. */
var typedArrayTags = {};
typedArrayTags[float32Tag] = typedArrayTags[float64Tag] =
    typedArrayTags[int8Tag] = typedArrayTags[int16Tag] =
        typedArrayTags[int32Tag] = typedArrayTags[uint8Tag] =
            typedArrayTags[uint8ClampedTag] = typedArrayTags[uint16Tag] =
                typedArrayTags[uint32Tag] = true;
typedArrayTags[argsTag$1] = typedArrayTags[arrayTag] =
    typedArrayTags[arrayBufferTag] = typedArrayTags[boolTag] =
        typedArrayTags[dataViewTag] = typedArrayTags[dateTag] =
            typedArrayTags[errorTag] = typedArrayTags[funcTag$1] =
                typedArrayTags[mapTag] = typedArrayTags[numberTag] =
                    typedArrayTags[objectTag] = typedArrayTags[regexpTag] =
                        typedArrayTags[setTag] = typedArrayTags[stringTag] =
                            typedArrayTags[weakMapTag] = false;

/**
 * The base implementation of `_.isTypedArray` without Node.js optimizations.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a typed array, else `false`.
 */
function baseIsTypedArray(value) {
    return isObjectLike_1(value) &&
        isLength_1(value.length) && !!typedArrayTags[_baseGetTag(value)];
}

var _baseIsTypedArray = baseIsTypedArray;

/**
 * The base implementation of `_.unary` without support for storing metadata.
 *
 * @private
 * @param {Function} func The function to cap arguments for.
 * @returns {Function} Returns the new capped function.
 */
function baseUnary(func) {
    return function(value) {
        return func(value);
    };
}

var _baseUnary = baseUnary;

var _nodeUtil = createCommonjsModule(function (module, exports) {
    /** Detect free variable `exports`. */
    var freeExports = 'object' == 'object' && exports && !exports.nodeType && exports;

    /** Detect free variable `module`. */
    var freeModule = freeExports && 'object' == 'object' && module && !module.nodeType && module;

    /** Detect the popular CommonJS extension `module.exports`. */
    var moduleExports = freeModule && freeModule.exports === freeExports;

    /** Detect free variable `process` from Node.js. */
    var freeProcess = moduleExports && _freeGlobal.process;

    /** Used to access faster Node.js helpers. */
    var nodeUtil = (function() {
        try {
            return freeProcess && freeProcess.binding && freeProcess.binding('util');
        } catch (e) {}
    }());

    module.exports = nodeUtil;
});

/* Node.js helper references. */
var nodeIsTypedArray = _nodeUtil && _nodeUtil.isTypedArray;

/**
 * Checks if `value` is classified as a typed array.
 *
 * @static
 * @memberOf _
 * @since 3.0.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a typed array, else `false`.
 * @example
 *
 * _.isTypedArray(new Uint8Array);
 * // => true
 *
 * _.isTypedArray([]);
 * // => false
 */
var isTypedArray = nodeIsTypedArray ? _baseUnary(nodeIsTypedArray) : _baseIsTypedArray;

var isTypedArray_1 = isTypedArray;

/** Used for built-in method references. */
var objectProto$4 = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty$5 = objectProto$4.hasOwnProperty;

/**
 * Creates an array of the enumerable property names of the array-like `value`.
 *
 * @private
 * @param {*} value The value to query.
 * @param {boolean} inherited Specify returning inherited property names.
 * @returns {Array} Returns the array of property names.
 */
function arrayLikeKeys(value, inherited) {
    var isArr = isArray_1(value),
        isArg = !isArr && isArguments_1(value),
        isBuff = !isArr && !isArg && isBuffer_1(value),
        isType = !isArr && !isArg && !isBuff && isTypedArray_1(value),
        skipIndexes = isArr || isArg || isBuff || isType,
        result = skipIndexes ? _baseTimes(value.length, String) : [],
        length = result.length;

    for (var key in value) {
        if ((inherited || hasOwnProperty$5.call(value, key)) &&
            !(skipIndexes && (
                // Safari 9 has enumerable `arguments.length` in strict mode.
                key == 'length' ||
                // Node.js 0.10 has enumerable non-index properties on buffers.
                (isBuff && (key == 'offset' || key == 'parent')) ||
                // PhantomJS 2 has enumerable non-index properties on typed arrays.
                (isType && (key == 'buffer' || key == 'byteLength' || key == 'byteOffset')) ||
                // Skip index properties.
                _isIndex(key, length)
            ))) {
            result.push(key);
        }
    }
    return result;
}

var _arrayLikeKeys = arrayLikeKeys;

/** Used for built-in method references. */
var objectProto$7 = Object.prototype;

/**
 * Checks if `value` is likely a prototype object.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a prototype, else `false`.
 */
function isPrototype(value) {
    var Ctor = value && value.constructor,
        proto = (typeof Ctor == 'function' && Ctor.prototype) || objectProto$7;

    return value === proto;
}

var _isPrototype = isPrototype;

/**
 * This function is like
 * [`Object.keys`](http://ecma-international.org/ecma-262/7.0/#sec-object.keys)
 * except that it includes inherited enumerable properties.
 *
 * @private
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of property names.
 */
function nativeKeysIn(object) {
    var result = [];
    if (object != null) {
        for (var key in Object(object)) {
            result.push(key);
        }
    }
    return result;
}

var _nativeKeysIn = nativeKeysIn;

/** Used for built-in method references. */
var objectProto$6 = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty$7 = objectProto$6.hasOwnProperty;

/**
 * The base implementation of `_.keysIn` which doesn't treat sparse arrays as dense.
 *
 * @private
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of property names.
 */
function baseKeysIn(object) {
    if (!isObject_1(object)) {
        return _nativeKeysIn(object);
    }
    var isProto = _isPrototype(object),
        result = [];

    for (var key in object) {
        if (!(key == 'constructor' && (isProto || !hasOwnProperty$7.call(object, key)))) {
            result.push(key);
        }
    }
    return result;
}

var _baseKeysIn = baseKeysIn;

/**
 * Creates an array of the own and inherited enumerable property names of `object`.
 *
 * **Note:** Non-object values are coerced to objects.
 *
 * @static
 * @memberOf _
 * @since 3.0.0
 * @category Object
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of property names.
 * @example
 *
 * function Foo() {
 *   this.a = 1;
 *   this.b = 2;
 * }
 *
 * Foo.prototype.c = 3;
 *
 * _.keysIn(new Foo);
 * // => ['a', 'b', 'c'] (iteration order is not guaranteed)
 */
function keysIn(object) {
    return isArrayLike_1(object) ? _arrayLikeKeys(object, true) : _baseKeysIn(object);
}

var keysIn_1 = keysIn;

/**
 * This method is like `_.assignIn` except that it accepts `customizer`
 * which is invoked to produce the assigned values. If `customizer` returns
 * `undefined`, assignment is handled by the method instead. The `customizer`
 * is invoked with five arguments: (objValue, srcValue, key, object, source).
 *
 * **Note:** This method mutates `object`.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @alias extendWith
 * @category Object
 * @param {Object} object The destination object.
 * @param {...Object} sources The source objects.
 * @param {Function} [customizer] The function to customize assigned values.
 * @returns {Object} Returns `object`.
 * @see _.assignWith
 * @example
 *
 * function customizer(objValue, srcValue) {
 *   return _.isUndefined(objValue) ? srcValue : objValue;
 * }
 *
 * var defaults = _.partialRight(_.assignInWith, customizer);
 *
 * defaults({ 'a': 1 }, { 'b': 2 }, { 'a': 3 });
 * // => { 'a': 1, 'b': 2 }
 */
var assignInWith = _createAssigner(function(object, source, srcIndex, customizer) {
    _copyObject(source, keysIn_1(source), object, customizer);
});

var assignInWith_1 = assignInWith;

/**
 * Creates a unary function that invokes `func` with its argument transformed.
 *
 * @private
 * @param {Function} func The function to wrap.
 * @param {Function} transform The argument transform.
 * @returns {Function} Returns the new function.
 */
function overArg(func, transform) {
    return function(arg) {
        return func(transform(arg));
    };
}

var _overArg = overArg;

/** Built-in value references. */
var getPrototype = _overArg(Object.getPrototypeOf, Object);

var _getPrototype = getPrototype;

/** `Object#toString` result references. */
var objectTag$1 = '[object Object]';

/** Used for built-in method references. */
var funcProto$2 = Function.prototype;
var objectProto$8 = Object.prototype;

/** Used to resolve the decompiled source of functions. */
var funcToString$2 = funcProto$2.toString;

/** Used to check objects for own properties. */
var hasOwnProperty$8 = objectProto$8.hasOwnProperty;

/** Used to infer the `Object` constructor. */
var objectCtorString = funcToString$2.call(Object);

/**
 * Checks if `value` is a plain object, that is, an object created by the
 * `Object` constructor or one with a `[[Prototype]]` of `null`.
 *
 * @static
 * @memberOf _
 * @since 0.8.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a plain object, else `false`.
 * @example
 *
 * function Foo() {
 *   this.a = 1;
 * }
 *
 * _.isPlainObject(new Foo);
 * // => false
 *
 * _.isPlainObject([1, 2, 3]);
 * // => false
 *
 * _.isPlainObject({ 'x': 0, 'y': 0 });
 * // => true
 *
 * _.isPlainObject(Object.create(null));
 * // => true
 */
function isPlainObject$1(value) {
    if (!isObjectLike_1(value) || _baseGetTag(value) != objectTag$1) {
        return false;
    }
    var proto = _getPrototype(value);
    if (proto === null) {
        return true;
    }
    var Ctor = hasOwnProperty$8.call(proto, 'constructor') && proto.constructor;
    return typeof Ctor == 'function' && Ctor instanceof Ctor &&
        funcToString$2.call(Ctor) == objectCtorString;
}

var isPlainObject_1 = isPlainObject$1;

/** `Object#toString` result references. */
var domExcTag = '[object DOMException]';
var errorTag$1 = '[object Error]';

/**
 * Checks if `value` is an `Error`, `EvalError`, `RangeError`, `ReferenceError`,
 * `SyntaxError`, `TypeError`, or `URIError` object.
 *
 * @static
 * @memberOf _
 * @since 3.0.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is an error object, else `false`.
 * @example
 *
 * _.isError(new Error);
 * // => true
 *
 * _.isError(Error);
 * // => false
 */
function isError$1(value) {
    if (!isObjectLike_1(value)) {
        return false;
    }
    var tag = _baseGetTag(value);
    return tag == errorTag$1 || tag == domExcTag ||
        (typeof value.message == 'string' && typeof value.name == 'string' && !isPlainObject_1(value));
}

var isError_1 = isError$1;

/**
 * Attempts to invoke `func`, returning either the result or the caught error
 * object. Any additional arguments are provided to `func` when it's invoked.
 *
 * @static
 * @memberOf _
 * @since 3.0.0
 * @category Util
 * @param {Function} func The function to attempt.
 * @param {...*} [args] The arguments to invoke `func` with.
 * @returns {*} Returns the `func` result or error object.
 * @example
 *
 * // Avoid throwing errors for invalid selectors.
 * var elements = _.attempt(function(selector) {
 *   return document.querySelectorAll(selector);
 * }, '>_>');
 *
 * if (_.isError(elements)) {
 *   elements = [];
 * }
 */
var attempt = _baseRest(function(func, args) {
    try {
        return _apply(func, undefined, args);
    } catch (e) {
        return isError_1(e) ? e : new Error(e);
    }
});

var attempt_1 = attempt;

/**
 * A specialized version of `_.map` for arrays without support for iteratee
 * shorthands.
 *
 * @private
 * @param {Array} [array] The array to iterate over.
 * @param {Function} iteratee The function invoked per iteration.
 * @returns {Array} Returns the new mapped array.
 */
function arrayMap(array, iteratee) {
    var index = -1,
        length = array == null ? 0 : array.length,
        result = Array(length);

    while (++index < length) {
        result[index] = iteratee(array[index], index, array);
    }
    return result;
}

var _arrayMap = arrayMap;

/**
 * The base implementation of `_.values` and `_.valuesIn` which creates an
 * array of `object` property values corresponding to the property names
 * of `props`.
 *
 * @private
 * @param {Object} object The object to query.
 * @param {Array} props The property names to get values for.
 * @returns {Object} Returns the array of property values.
 */
function baseValues(object, props) {
    return _arrayMap(props, function(key) {
        return object[key];
    });
}

var _baseValues = baseValues;

/** Used for built-in method references. */
var objectProto$9 = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty$9 = objectProto$9.hasOwnProperty;

/**
 * Used by `_.defaults` to customize its `_.assignIn` use to assign properties
 * of source objects to the destination object for all destination properties
 * that resolve to `undefined`.
 *
 * @private
 * @param {*} objValue The destination value.
 * @param {*} srcValue The source value.
 * @param {string} key The key of the property to assign.
 * @param {Object} object The parent object of `objValue`.
 * @returns {*} Returns the value to assign.
 */
function customDefaultsAssignIn(objValue, srcValue, key, object) {
    if (objValue === undefined ||
        (eq_1(objValue, objectProto$9[key]) && !hasOwnProperty$9.call(object, key))) {
        return srcValue;
    }
    return objValue;
}

var _customDefaultsAssignIn = customDefaultsAssignIn;

/** Used to escape characters for inclusion in compiled string literals. */
var stringEscapes = {
    '\\': '\\',
    "'": "'",
    '\n': 'n',
    '\r': 'r',
    '\u2028': 'u2028',
    '\u2029': 'u2029'
};

/**
 * Used by `_.template` to escape characters for inclusion in compiled string literals.
 *
 * @private
 * @param {string} chr The matched character to escape.
 * @returns {string} Returns the escaped character.
 */
function escapeStringChar(chr) {
    return '\\' + stringEscapes[chr];
}

var _escapeStringChar = escapeStringChar;

/* Built-in method references for those with the same name as other `lodash` methods. */
var nativeKeys = _overArg(Object.keys, Object);

var _nativeKeys = nativeKeys;

/** Used for built-in method references. */
var objectProto$10 = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty$10 = objectProto$10.hasOwnProperty;

/**
 * The base implementation of `_.keys` which doesn't treat sparse arrays as dense.
 *
 * @private
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of property names.
 */
function baseKeys(object) {
    if (!_isPrototype(object)) {
        return _nativeKeys(object);
    }
    var result = [];
    for (var key in Object(object)) {
        if (hasOwnProperty$10.call(object, key) && key != 'constructor') {
            result.push(key);
        }
    }
    return result;
}

var _baseKeys = baseKeys;

/**
 * Creates an array of the own enumerable property names of `object`.
 *
 * **Note:** Non-object values are coerced to objects. See the
 * [ES spec](http://ecma-international.org/ecma-262/7.0/#sec-object.keys)
 * for more details.
 *
 * @static
 * @since 0.1.0
 * @memberOf _
 * @category Object
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of property names.
 * @example
 *
 * function Foo() {
 *   this.a = 1;
 *   this.b = 2;
 * }
 *
 * Foo.prototype.c = 3;
 *
 * _.keys(new Foo);
 * // => ['a', 'b'] (iteration order is not guaranteed)
 *
 * _.keys('hi');
 * // => ['0', '1']
 */
function keys$1(object) {
    return isArrayLike_1(object) ? _arrayLikeKeys(object) : _baseKeys(object);
}

var keys_1 = keys$1;

/** Used to match template delimiters. */
var reInterpolate = /<%=([\s\S]+?)%>/g;

var _reInterpolate = reInterpolate;

/**
 * The base implementation of `_.propertyOf` without support for deep paths.
 *
 * @private
 * @param {Object} object The object to query.
 * @returns {Function} Returns the new accessor function.
 */
function basePropertyOf(object) {
    return function(key) {
        return object == null ? undefined : object[key];
    };
}

var _basePropertyOf = basePropertyOf;

/** Used to map characters to HTML entities. */
var htmlEscapes = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#39;'
};

/**
 * Used by `_.escape` to convert characters to HTML entities.
 *
 * @private
 * @param {string} chr The matched character to escape.
 * @returns {string} Returns the escaped character.
 */
var escapeHtmlChar = _basePropertyOf(htmlEscapes);

var _escapeHtmlChar = escapeHtmlChar;

/** `Object#toString` result references. */
var symbolTag = '[object Symbol]';

/**
 * Checks if `value` is classified as a `Symbol` primitive or object.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a symbol, else `false`.
 * @example
 *
 * _.isSymbol(Symbol.iterator);
 * // => true
 *
 * _.isSymbol('abc');
 * // => false
 */
function isSymbol$1(value) {
    return typeof value == 'symbol' ||
        (isObjectLike_1(value) && _baseGetTag(value) == symbolTag);
}

var isSymbol_1 = isSymbol$1;

/** Used as references for various `Number` constants. */
var INFINITY = 1 / 0;

/** Used to convert symbols to primitives and strings. */
var symbolProto = _Symbol ? _Symbol.prototype : undefined;
var symbolToString = symbolProto ? symbolProto.toString : undefined;

/**
 * The base implementation of `_.toString` which doesn't convert nullish
 * values to empty strings.
 *
 * @private
 * @param {*} value The value to process.
 * @returns {string} Returns the string.
 */
function baseToString(value) {
    // Exit early for strings to avoid a performance hit in some environments.
    if (typeof value == 'string') {
        return value;
    }
    if (isArray_1(value)) {
        // Recursively convert values (susceptible to call stack limits).
        return _arrayMap(value, baseToString) + '';
    }
    if (isSymbol_1(value)) {
        return symbolToString ? symbolToString.call(value) : '';
    }
    var result = (value + '');
    return (result == '0' && (1 / value) == -INFINITY) ? '-0' : result;
}

var _baseToString = baseToString;

/**
 * Converts `value` to a string. An empty string is returned for `null`
 * and `undefined` values. The sign of `-0` is preserved.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to convert.
 * @returns {string} Returns the converted string.
 * @example
 *
 * _.toString(null);
 * // => ''
 *
 * _.toString(-0);
 * // => '-0'
 *
 * _.toString([1, 2, 3]);
 * // => '1,2,3'
 */
function toString$2(value) {
    return value == null ? '' : _baseToString(value);
}

var toString_1 = toString$2;

/** Used to match HTML entities and HTML characters. */
var reUnescapedHtml = /[&<>"']/g;
var reHasUnescapedHtml = RegExp(reUnescapedHtml.source);

/**
 * Converts the characters "&", "<", ">", '"', and "'" in `string` to their
 * corresponding HTML entities.
 *
 * **Note:** No other characters are escaped. To escape additional
 * characters use a third-party library like [_he_](https://mths.be/he).
 *
 * Though the ">" character is escaped for symmetry, characters like
 * ">" and "/" don't need escaping in HTML and have no special meaning
 * unless they're part of a tag or unquoted attribute value. See
 * [Mathias Bynens's article](https://mathiasbynens.be/notes/ambiguous-ampersands)
 * (under "semi-related fun fact") for more details.
 *
 * When working with HTML you should always
 * [quote attribute values](http://wonko.com/post/html-escaping) to reduce
 * XSS vectors.
 *
 * @static
 * @since 0.1.0
 * @memberOf _
 * @category String
 * @param {string} [string=''] The string to escape.
 * @returns {string} Returns the escaped string.
 * @example
 *
 * _.escape('fred, barney, & pebbles');
 * // => 'fred, barney, &amp; pebbles'
 */
function escape(string) {
    string = toString_1(string);
    return (string && reHasUnescapedHtml.test(string))
        ? string.replace(reUnescapedHtml, _escapeHtmlChar)
        : string;
}

var _escape = escape;

/** Used to match template delimiters. */
var reEscape = /<%-([\s\S]+?)%>/g;

var _reEscape = reEscape;

/** Used to match template delimiters. */
var reEvaluate = /<%([\s\S]+?)%>/g;

var _reEvaluate = reEvaluate;

/**
 * By default, the template delimiters used by lodash are like those in
 * embedded Ruby (ERB) as well as ES2015 template strings. Change the
 * following template settings to use alternative delimiters.
 *
 * @static
 * @memberOf _
 * @type {Object}
 */
var templateSettings = {

    /**
     * Used to detect `data` property values to be HTML-escaped.
     *
     * @memberOf _.templateSettings
     * @type {RegExp}
     */
    'escape': _reEscape,

    /**
     * Used to detect code to be evaluated.
     *
     * @memberOf _.templateSettings
     * @type {RegExp}
     */
    'evaluate': _reEvaluate,

    /**
     * Used to detect `data` property values to inject.
     *
     * @memberOf _.templateSettings
     * @type {RegExp}
     */
    'interpolate': _reInterpolate,

    /**
     * Used to reference the data object in the template text.
     *
     * @memberOf _.templateSettings
     * @type {string}
     */
    'variable': '',

    /**
     * Used to import variables into the compiled template.
     *
     * @memberOf _.templateSettings
     * @type {Object}
     */
    'imports': {

        /**
         * A reference to the `lodash` function.
         *
         * @memberOf _.templateSettings.imports
         * @type {Function}
         */
        '_': { 'escape': _escape }
    }
};

var templateSettings_1 = templateSettings;

/** Used to match empty string literals in compiled template source. */
var reEmptyStringLeading = /\b__p \+= '';/g;
var reEmptyStringMiddle = /\b(__p \+=) '' \+/g;
var reEmptyStringTrailing = /(__e\(.*?\)|\b__t\)) \+\n'';/g;

/**
 * Used to match
 * [ES template delimiters](http://ecma-international.org/ecma-262/7.0/#sec-template-literal-lexical-components).
 */
var reEsTemplate = /\$\{([^\\}]*(?:\\.[^\\}]*)*)\}/g;

/** Used to ensure capturing order of template delimiters. */
var reNoMatch = /($^)/;

/** Used to match unescaped characters in compiled string literals. */
var reUnescapedString = /['\n\r\u2028\u2029\\]/g;

/**
 * Creates a compiled template function that can interpolate data properties
 * in "interpolate" delimiters, HTML-escape interpolated data properties in
 * "escape" delimiters, and execute JavaScript in "evaluate" delimiters. Data
 * properties may be accessed as free variables in the template. If a setting
 * object is given, it takes precedence over `_.templateSettings` values.
 *
 * **Note:** In the development build `_.template` utilizes
 * [sourceURLs](http://www.html5rocks.com/en/tutorials/developertools/sourcemaps/#toc-sourceurl)
 * for easier debugging.
 *
 * For more information on precompiling templates see
 * [lodash's custom builds documentation](https://lodash.com/custom-builds).
 *
 * For more information on Chrome extension sandboxes see
 * [Chrome's extensions documentation](https://developer.chrome.com/extensions/sandboxingEval).
 *
 * @static
 * @since 0.1.0
 * @memberOf _
 * @category String
 * @param {string} [string=''] The template string.
 * @param {Object} [options={}] The options object.
 * @param {RegExp} [options.escape=_.templateSettings.escape]
 *  The HTML "escape" delimiter.
 * @param {RegExp} [options.evaluate=_.templateSettings.evaluate]
 *  The "evaluate" delimiter.
 * @param {Object} [options.imports=_.templateSettings.imports]
 *  An object to import into the template as free variables.
 * @param {RegExp} [options.interpolate=_.templateSettings.interpolate]
 *  The "interpolate" delimiter.
 * @param {string} [options.sourceURL='templateSources[n]']
 *  The sourceURL of the compiled template.
 * @param {string} [options.variable='obj']
 *  The data object variable name.
 * @param- {Object} [guard] Enables use as an iteratee for methods like `_.map`.
 * @returns {Function} Returns the compiled template function.
 * @example
 *
 * // Use the "interpolate" delimiter to create a compiled template.
 * var compiled = _.template('hello <%= user %>!');
 * compiled({ 'user': 'fred' });
 * // => 'hello fred!'
 *
 * // Use the HTML "escape" delimiter to escape data property values.
 * var compiled = _.template('<b><%- value %></b>');
 * compiled({ 'value': '<script>' });
 * // => '<b>&lt;script&gt;</b>'
 *
 * // Use the "evaluate" delimiter to execute JavaScript and generate HTML.
 * var compiled = _.template('<% _.forEach(users, function(user) { %><li><%- user %></li><% }); %>');
 * compiled({ 'users': ['fred', 'barney'] });
 * // => '<li>fred</li><li>barney</li>'
 *
 * // Use the internal `print` function in "evaluate" delimiters.
 * var compiled = _.template('<% print("hello " + user); %>!');
 * compiled({ 'user': 'barney' });
 * // => 'hello barney!'
 *
 * // Use the ES template literal delimiter as an "interpolate" delimiter.
 * // Disable support by replacing the "interpolate" delimiter.
 * var compiled = _.template('hello ${ user }!');
 * compiled({ 'user': 'pebbles' });
 * // => 'hello pebbles!'
 *
 * // Use backslashes to treat delimiters as plain text.
 * var compiled = _.template('<%= "\\<%- value %\\>" %>');
 * compiled({ 'value': 'ignored' });
 * // => '<%- value %>'
 *
 * // Use the `imports` option to import `jQuery` as `jq`.
 * var text = '<% jq.each(users, function(user) { %><li><%- user %></li><% }); %>';
 * var compiled = _.template(text, { 'imports': { 'jq': jQuery } });
 * compiled({ 'users': ['fred', 'barney'] });
 * // => '<li>fred</li><li>barney</li>'
 *
 * // Use the `sourceURL` option to specify a custom sourceURL for the template.
 * var compiled = _.template('hello <%= user %>!', { 'sourceURL': '/basic/greeting.jst' });
 * compiled(data);
 * // => Find the source of "greeting.jst" under the Sources tab or Resources panel of the web inspector.
 *
 * // Use the `variable` option to ensure a with-statement isn't used in the compiled template.
 * var compiled = _.template('hi <%= data.user %>!', { 'variable': 'data' });
 * compiled.source;
 * // => function(data) {
 * //   var __t, __p = '';
 * //   __p += 'hi ' + ((__t = ( data.user )) == null ? '' : __t) + '!';
 * //   return __p;
 * // }
 *
 * // Use custom template delimiters.
 * _.templateSettings.interpolate = /{{([\s\S]+?)}}/g;
 * var compiled = _.template('hello {{ user }}!');
 * compiled({ 'user': 'mustache' });
 * // => 'hello mustache!'
 *
 * // Use the `source` property to inline compiled templates for meaningful
 * // line numbers in error messages and stack traces.
 * fs.writeFileSync(path.join(process.cwd(), 'jst.js'), '\
 *   var JST = {\
 *     "main": ' + _.template(mainText).source + '\
 *   };\
 * ');
 */
function template(string, options, guard) {
    // Based on John Resig's `tmpl` implementation
    // (http://ejohn.org/blog/javascript-micro-templating/)
    // and Laura Doktorova's doT.js (https://github.com/olado/doT).
    var settings = templateSettings_1.imports._.templateSettings || templateSettings_1;

    if (guard && _isIterateeCall(string, options, guard)) {
        options = undefined;
    }
    string = toString_1(string);
    options = assignInWith_1({}, options, settings, _customDefaultsAssignIn);

    var imports = assignInWith_1({}, options.imports, settings.imports, _customDefaultsAssignIn),
        importsKeys = keys_1(imports),
        importsValues = _baseValues(imports, importsKeys);

    var isEscaping,
        isEvaluating,
        index = 0,
        interpolate = options.interpolate || reNoMatch,
        source = "__p += '";

    // Compile the regexp to match each delimiter.
    var reDelimiters = RegExp(
        (options.escape || reNoMatch).source + '|' +
        interpolate.source + '|' +
        (interpolate === _reInterpolate ? reEsTemplate : reNoMatch).source + '|' +
        (options.evaluate || reNoMatch).source + '|$'
        , 'g');

    // Use a sourceURL for easier debugging.
    var sourceURL = 'sourceURL' in options ? '//# sourceURL=' + options.sourceURL + '\n' : '';

    string.replace(reDelimiters, function(match, escapeValue, interpolateValue, esTemplateValue, evaluateValue, offset) {
        interpolateValue || (interpolateValue = esTemplateValue);

        // Escape characters that can't be included in string literals.
        source += string.slice(index, offset).replace(reUnescapedString, _escapeStringChar);

        // Replace delimiters with snippets.
        if (escapeValue) {
            isEscaping = true;
            source += "' +\n__e(" + escapeValue + ") +\n'";
        }
        if (evaluateValue) {
            isEvaluating = true;
            source += "';\n" + evaluateValue + ";\n__p += '";
        }
        if (interpolateValue) {
            source += "' +\n((__t = (" + interpolateValue + ")) == null ? '' : __t) +\n'";
        }
        index = offset + match.length;

        // The JS engine embedded in Adobe products needs `match` returned in
        // order to produce the correct `offset` value.
        return match;
    });

    source += "';\n";

    // If `variable` is not specified wrap a with-statement around the generated
    // code to add the data object to the top of the scope chain.
    var variable = options.variable;
    if (!variable) {
        source = 'with (obj) {\n' + source + '\n}\n';
    }
    // Cleanup code by stripping empty strings.
    source = (isEvaluating ? source.replace(reEmptyStringLeading, '') : source)
        .replace(reEmptyStringMiddle, '$1')
        .replace(reEmptyStringTrailing, '$1;');

    // Frame code as the function body.
    source = 'function(' + (variable || 'obj') + ') {\n' +
        (variable
                ? ''
                : 'obj || (obj = {});\n'
        ) +
        "var __t, __p = ''" +
        (isEscaping
                ? ', __e = _.escape'
                : ''
        ) +
        (isEvaluating
                ? ', __j = Array.prototype.join;\n' +
                "function print() { __p += __j.call(arguments, '') }\n"
                : ';\n'
        ) +
        source +
        'return __p\n}';

    var result = attempt_1(function() {
        return Function(importsKeys, sourceURL + 'return ' + source)
            .apply(undefined, importsValues);
    });

    // Provide the compiled function's source by its `toString` method or
    // the `source` property as a convenience for inlining compiled templates.
    result.source = source;
    if (isError_1(result)) {
        throw result;
    }
    return result;
}

var template_1 = template;

/*  */

//const compile = require('lodash.template')
var compileOptions = {
    escape: /{{[^{]([\s\S]+?)[^}]}}/g,
    interpolate: /{{{([\s\S]+?)}}}/g
};



function parseTemplate (
    template,
    contentPlaceholder
) {
    if ( contentPlaceholder === void 0 ) contentPlaceholder = '<!--vue-ssr-outlet-->';

    if (typeof template === 'object') {
        return template
    }

    var i = template.indexOf('</head>');
    var j = template.indexOf(contentPlaceholder);

    if (j < 0) {
        throw new Error("Content placeholder not found in template.")
    }

    if (i < 0) {
        i = template.indexOf('<body>');
        if (i < 0) {
            i = j;
        }
    }

    return {
        head: template_1(template.slice(0, i), compileOptions),
        neck: template_1(template.slice(i, j), compileOptions),
        tail: template_1(template.slice(j + contentPlaceholder.length), compileOptions)
    }
}

/*  */

/**
 * Creates a mapper that maps components used during a server-side render
 * to async chunk files in the client-side build, so that we can inline them
 * directly in the rendered HTML to avoid waterfall requests.
 */





function createMapper (
    clientManifest
) {
    var map = createMap(clientManifest);
    // map server-side moduleIds to client-side files
    return function mapper (moduleIds) {
        var res = new Set();
        for (var i = 0; i < moduleIds.length; i++) {
            var mapped = map.get(moduleIds[i]);
            if (mapped) {
                for (var j = 0; j < mapped.length; j++) {
                    res.add(mapped[j]);
                }
            }
        }
        return Array.from(res)
    }
}

function createMap (clientManifest) {
    var map = new Map();
    Object.keys(clientManifest.modules).forEach(function (id) {
        map.set(id, mapIdToFile(id, clientManifest));
    });
    return map
}

function mapIdToFile (id, clientManifest) {
    var files = [];
    var fileIndices = clientManifest.modules[id];
    if (fileIndices) {
        fileIndices.forEach(function (index) {
            var file = clientManifest.all[index];
            // only include async files or non-js assets
            if (clientManifest.async.indexOf(file) > -1 || !(/\.js($|\?)/.test(file))) {
                files.push(file);
            }
        });
    }
    return files
}

/*  */

// const path = require('path')
// const serialize = require('serialize-javascript')
var TemplateRenderer = function TemplateRenderer (options) {
    this.options = options;
    this.inject = options.inject !== false;
    // if no template option is provided, the renderer is created
    // as a utility object for rendering assets like preload links and scripts.
    this.parsedTemplate = options.template
        ? parseTemplate(options.template)
        : null;

    // extra functionality with client manifest
    if (options.clientManifest) {
        var clientManifest = this.clientManifest = options.clientManifest;
        this.publicPath = clientManifest.publicPath.replace(/\/$/, '');
        // preload/prefetch drectives
        this.preloadFiles = clientManifest.initial;
        this.prefetchFiles = clientManifest.async;
        // initial async chunk mapping
        this.mapFiles = createMapper(clientManifest);
    }
};

TemplateRenderer.prototype.bindRenderFns = function bindRenderFns (context) {
    var renderer = this;['ResourceHints', 'State', 'Scripts', 'Styles'].forEach(function (type) {
        context[("render" + type)] = renderer[("render" + type)].bind(renderer, context);
    });
    // also expose getPreloadFiles, useful for HTTP/2 push
    context.getPreloadFiles = renderer.getPreloadFiles.bind(renderer, context);
};

// render synchronously given rendered app content and render context
TemplateRenderer.prototype.renderSync = function renderSync (content, context) {
    var template = this.parsedTemplate;
    if (!template) {
        throw new Error('renderSync cannot be called without a template.')
    }
    context = context || {};
    if (this.inject) {
        return (
            template.head(context) +
            (context.head || '') +
            this.renderResourceHints(context) +
            this.renderStyles(context) +
            template.neck(context) +
            content +
            this.renderState(context) +
            this.renderScripts(context) +
            template.tail(context)
        )
    } else {
        return (
            template.head(context) +
            template.neck(context) +
            content +
            template.tail(context)
        )
    }
};

TemplateRenderer.prototype.renderStyles = function renderStyles (context) {
    var this$1 = this;

    var cssFiles = this.clientManifest
        ? this.clientManifest.all.filter(isCSS)
        : [];
    return (
        // render links for css files
        (cssFiles.length
            ? cssFiles.map(function (file) { return ("<link rel=\"stylesheet\" href=\"" + (this$1.publicPath) + "/" + file + "\">"); }).join('')
            : '') +
        // context.styles is a getter exposed by vue-style-loader which contains
        // the inline component styles collected during SSR
        (context.styles || '')
    )
};

TemplateRenderer.prototype.renderResourceHints = function renderResourceHints (context) {
    return this.renderPreloadLinks(context) + this.renderPrefetchLinks(context)
};

TemplateRenderer.prototype.getPreloadFiles = function getPreloadFiles (context) {
    var usedAsyncFiles = this.getUsedAsyncFiles(context);
    if (this.preloadFiles || usedAsyncFiles) {
        return (this.preloadFiles || []).concat(usedAsyncFiles || []).map(function (file) {
            var withoutQuery = file.replace(/\?.*/, '');
            var extension = extname(withoutQuery).slice(1);
            return {
                file: file,
                extension: extension,
                fileWithoutQuery: withoutQuery,
                asType: getPreloadType(extension)
            }
        })
    } else {
        return []
    }
};

TemplateRenderer.prototype.renderPreloadLinks = function renderPreloadLinks (context) {
    var this$1 = this;

    var files = this.getPreloadFiles(context);
    if (files.length) {
        return files.map(function (ref) {
            var file = ref.file;
            var extension = ref.extension;
            var fileWithoutQuery = ref.fileWithoutQuery;
            var asType = ref.asType;

            var extra = '';
            var shouldPreload = this$1.options.shouldPreload;
            // by default, we only preload scripts or css
            if (!shouldPreload && asType !== 'script' && asType !== 'style') {
                return ''
            }
            // user wants to explicitly control what to preload
            if (shouldPreload && !shouldPreload(fileWithoutQuery, asType)) {
                return ''
            }
            if (asType === 'font') {
                extra = " type=\"font/" + extension + "\" crossorigin";
            }
            return ("<link rel=\"preload\" href=\"" + (this$1.publicPath) + "/" + file + "\"" + (asType !== '' ? (" as=\"" + asType + "\"") : '') + extra + ">")
        }).join('')
    } else {
        return ''
    }
};

TemplateRenderer.prototype.renderPrefetchLinks = function renderPrefetchLinks (context) {
    var this$1 = this;

    if (this.prefetchFiles) {
        var usedAsyncFiles = this.getUsedAsyncFiles(context);
        var alreadyRendered = function (file) {
            return usedAsyncFiles && usedAsyncFiles.some(function (f) { return f === file; })
        };
        return this.prefetchFiles.map(function (file) {
            if (!alreadyRendered(file)) {
                return ("<link rel=\"prefetch\" href=\"" + (this$1.publicPath) + "/" + file + "\" as=\"script\">")
            } else {
                return ''
            }
        }).join('')
    } else {
        return ''
    }
};

TemplateRenderer.prototype.renderState = function renderState (context, options) {
    var ref = options || {};
    var contextKey = ref.contextKey; if ( contextKey === void 0 ) contextKey = 'state';
    var windowKey = ref.windowKey; if ( windowKey === void 0 ) windowKey = '__INITIAL_STATE__';
    return context[contextKey]
        ? ("<script>window." + windowKey + "=" + (index_1(context[contextKey], { isJSON: true })) + "</script>")
        : ''
};

TemplateRenderer.prototype.renderScripts = function renderScripts (context) {
    var this$1 = this;

    if (this.clientManifest) {
        var initial = this.clientManifest.initial;
        var async = this.getUsedAsyncFiles(context);
        var needed = [initial[0]].concat(async || [], initial.slice(1));
        return needed.filter(isJS).map(function (file) {
            return ("<script src=\"" + (this$1.publicPath) + "/" + file + "\"></script>")
        }).join('')
    } else {
        return ''
    }
};

TemplateRenderer.prototype.getUsedAsyncFiles = function getUsedAsyncFiles (context) {
    if (!context._mappedfiles && context._registeredComponents && this.mapFiles) {
        context._mappedFiles = this.mapFiles(Array.from(context._registeredComponents));
    }
    return context._mappedFiles
};

// create a transform stream
TemplateRenderer.prototype.createStream = function createStream (context) {
    if (!this.parsedTemplate) {
        throw new Error('createStream cannot be called without a template.')
    }
    return new TemplateStream(this, this.parsedTemplate, context || {})
};

function getPreloadType (ext) {
    if (ext === 'js') {
        return 'script'
    } else if (ext === 'css') {
        return 'style'
    } else if (/jpe?g|png|svg|gif|webp|ico/.test(ext)) {
        return 'image'
    } else if (/woff2?|ttf|otf|eot/.test(ext)) {
        return 'font'
    } else {
        // not exhausting all possbilities here, but above covers common cases
        return ''
    }
}

/*  */

var RenderContext = function RenderContext (options) {
    this.userContext = options.userContext;
    this.activeInstance = options.activeInstance;
    this.renderStates = [];

    this.write = options.write;
    this.done = options.done;
    this.renderNode = options.renderNode;

    this.isUnaryTag = options.isUnaryTag;
    this.modules = options.modules;
    this.directives = options.directives;

    var cache = options.cache;
    if (cache && (!cache.get || !cache.set)) {
        throw new Error('renderer cache must implement at least get & set.')
    }
    this.cache = cache;
    this.get = cache && normalizeAsync(cache, 'get');
    this.has = cache && normalizeAsync(cache, 'has');

    this.next = this.next.bind(this);
};

RenderContext.prototype.next = function next () {
    var lastState = this.renderStates[this.renderStates.length - 1];
    if (isUndef(lastState)) {
        return this.done()
    }
    switch (lastState.type) {
        case 'Element':
            var children = lastState.children;
            var total = lastState.total;
            var rendered = lastState.rendered++;
            if (rendered < total) {
                this.renderNode(children[rendered], false, this);
            } else {
                this.renderStates.pop();
                this.write(lastState.endTag, this.next);
            }
            break
        case 'Component':
            this.renderStates.pop();
            this.activeInstance = lastState.prevActive;
            this.next();
            break
        case 'ComponentWithCache':
            this.renderStates.pop();
            var buffer = lastState.buffer;
            var bufferIndex = lastState.bufferIndex;
            var componentBuffer = lastState.componentBuffer;
            var key = lastState.key;
            var result = {
                html: buffer[bufferIndex],
                components: componentBuffer[bufferIndex]
            };
            this.cache.set(key, result);
            if (bufferIndex === 0) {
                // this is a top-level cached component,
                // exit caching mode.
                this.write.caching = false;
            } else {
                // parent component is also being cached,
                // merge self into parent's result
                buffer[bufferIndex - 1] += result.html;
                var prev = componentBuffer[bufferIndex - 1];
                result.components.forEach(function (c) { return prev.add(c); });
            }
            buffer.length = bufferIndex;
            componentBuffer.length = bufferIndex;
            this.next();
            break
    }
};

function normalizeAsync (cache, method) {
    var fn = cache[method];
    if (isUndef(fn)) {
        return
    } else if (fn.length > 1) {
        return function (key, cb) { return fn.call(cache, key, cb); }
    } else {
        return function (key, cb) { return cb(fn.call(cache, key)); }
    }
}

/*  */

//const { escape } = require('he')
var warned = Object.create(null);
var warnOnce = function (msg) {
    if (!warned[msg]) {
        warned[msg] = true;
        console.warn(("\n\u001b[31m" + msg + "\u001b[39m\n"));
    }
};

var compilationCache = Object.create(null);
var normalizeRender = function (vm) {
    var ref = vm.$options;
    var render = ref.render;
    var template = ref.template;
    if (isUndef(render)) {
        if (template) {
            var renderFns = (
                compilationCache[template] ||
                (compilationCache[template] = compileToFunctions(template))
            );
            Object.assign(vm.$options, renderFns);
        } else {
            throw new Error(
                ("render function or template not defined in component: " + (vm.$options.name || vm.$options._componentTag || 'anonymous'))
            )
        }
    }
};

function renderNode (node, isRoot, context) {
    if (isDef(node.componentOptions)) {
        renderComponent(node, isRoot, context);
    } else {
        if (isDef(node.tag)) {
            renderElement(node, isRoot, context);
        } else if (isTrue(node.isComment)) {
            context.write(
                ("<!--" + (node.text) + "-->"),
                context.next
            );
        } else {
            context.write(
                node.raw ? node.text : he_2(String(node.text)),
                context.next
            );
        }
    }
}

function renderComponent (node, isRoot, context) {
    var write = context.write;
    var next = context.next;
    var userContext = context.userContext;

    // check cache hit
    var Ctor = node.componentOptions.Ctor;
    var getKey = Ctor.options.serverCacheKey;
    var name = Ctor.options.name;

    // exposed by vue-loader, need to call this if cache hit because
    // component lifecycle hooks will not be called.
    var registerComponent = Ctor.options._ssrRegister;
    if (write.caching && isDef(registerComponent)) {
        write.componentBuffer[write.componentBuffer.length - 1].add(registerComponent);
    }

    var cache = context.cache;
    if (isDef(getKey) && isDef(cache) && isDef(name)) {
        var key = name + '::' + getKey(node.componentOptions.propsData);
        var has = context.has;
        var get = context.get;
        if (isDef(has)) {
            (has)(key, function (hit) {
                if (hit === true && isDef(get)) {
                    (get)(key, function (res) {
                        if (isDef(registerComponent)) {
                            registerComponent(userContext);
                        }
                        res.components.forEach(function (register) { return register(userContext); });
                        write(res.html, next);
                    });
                } else {
                    renderComponentWithCache(node, isRoot, key, context);
                }
            });
        } else if (isDef(get)) {
            (get)(key, function (res) {
                if (isDef(res)) {
                    if (isDef(registerComponent)) {
                        registerComponent(userContext);
                    }
                    res.components.forEach(function (register) { return register(userContext); });
                    write(res.html, next);
                } else {
                    renderComponentWithCache(node, isRoot, key, context);
                }
            });
        }
    } else {
        if (isDef(getKey) && isUndef(cache)) {
            warnOnce(
                "[vue-server-renderer] Component " + (Ctor.options.name || '(anonymous)') + " implemented serverCacheKey, " +
                'but no cache was provided to the renderer.'
            );
        }
        if (isDef(getKey) && isUndef(name)) {
            warnOnce(
                "[vue-server-renderer] Components that implement \"serverCacheKey\" " +
                "must also define a unique \"name\" option."
            );
        }
        renderComponentInner(node, isRoot, context);
    }
}

function renderComponentWithCache (node, isRoot, key, context) {
    var write = context.write;
    write.caching = true;
    var buffer = write.cacheBuffer;
    var bufferIndex = buffer.push('') - 1;
    var componentBuffer = write.componentBuffer;
    componentBuffer.push(new Set());
    context.renderStates.push({
        type: 'ComponentWithCache',
        key: key,
        buffer: buffer,
        bufferIndex: bufferIndex,
        componentBuffer: componentBuffer
    });
    renderComponentInner(node, isRoot, context);
}

function renderComponentInner (node, isRoot, context) {
    var prevActive = context.activeInstance;
    // expose userContext on vnode
    node.ssrContext = context.userContext;
    var child = context.activeInstance = createComponentInstanceForVnode(
        node,
        context.activeInstance
    );
    node.ssrContext = null;
    normalizeRender(child);
    var childNode = child._render();
    childNode.parent = node;
    context.renderStates.push({
        type: 'Component',
        prevActive: prevActive
    });
    renderNode(childNode, isRoot, context);
}

function renderElement (el, isRoot, context) {
    if (isTrue(isRoot)) {
        if (!el.data) { el.data = {}; }
        if (!el.data.attrs) { el.data.attrs = {}; }
        el.data.attrs[SSR_ATTR] = 'true';
    }
    var startTag = renderStartingTag(el, context);
    var endTag = "</" + (el.tag) + ">";
    var write = context.write;
    var next = context.next;
    if (context.isUnaryTag(el.tag)) {
        write(startTag, next);
    } else if (isUndef(el.children) || el.children.length === 0) {
        write(startTag + endTag, next);
    } else {
        var children = el.children;
        context.renderStates.push({
            type: 'Element',
            rendered: 0,
            total: children.length,
            endTag: endTag, children: children
        });
        write(startTag, next);
    }
}

function hasAncestorData (node) {
    var parentNode = node.parent;
    return isDef(parentNode) && (isDef(parentNode.data) || hasAncestorData(parentNode))
}

function getVShowDirectiveInfo (node) {
    var dir;
    var tmp;

    while (isDef(node)) {
        if (node.data && node.data.directives) {
            tmp = node.data.directives.find(function (dir) { return dir.name === 'show'; });
            if (tmp) {
                dir = tmp;
            }
        }
        node = node.parent;
    }
    return dir
}

function renderStartingTag (node, context) {
    var markup = "<" + (node.tag);
    var directives = context.directives;
    var modules = context.modules;

    // construct synthetic data for module processing
    // because modules like style also produce code by parent VNode data
    if (isUndef(node.data) && hasAncestorData(node)) {
        node.data = {};
    }
    if (isDef(node.data)) {
        // check directives
        var dirs = node.data.directives;
        if (dirs) {
            for (var i = 0; i < dirs.length; i++) {
                var name = dirs[i].name;
                var dirRenderer = directives[name];
                if (dirRenderer && name !== 'show') {
                    // directives mutate the node's data
                    // which then gets rendered by modules
                    dirRenderer(node, dirs[i]);
                }
            }
        }

        // v-show directive needs to be merged from parent to child
        var vshowDirectiveInfo = getVShowDirectiveInfo(node);
        if (vshowDirectiveInfo) {
            directives.show(node, vshowDirectiveInfo);
        }

        // apply other modules
        for (var i$1 = 0; i$1 < modules.length; i$1++) {
            var res = modules[i$1](node);
            if (res) {
                markup += res;
            }
        }
    }
    // attach scoped CSS ID
    var scopeId;
    var activeInstance = context.activeInstance;
    if (isDef(activeInstance) &&
        activeInstance !== node.context &&
        isDef(scopeId = activeInstance.$options._scopeId)) {
        markup += " " + ((scopeId));
    }
    while (isDef(node)) {
        if (isDef(scopeId = node.context.$options._scopeId)) {
            markup += " " + scopeId;
        }
        node = node.parent;
    }
    return markup + '>'
}

function createRenderFunction (
    modules,
    directives,
    isUnaryTag,
    cache
) {
    return function render (
        component,
        write,
        userContext,
        done
    ) {
        warned = Object.create(null);
        var context = new RenderContext({
            activeInstance: component,
            userContext: userContext,
            write: write, done: done, renderNode: renderNode,
            isUnaryTag: isUnaryTag, modules: modules, directives: directives,
            cache: cache
        });
        normalizeRender(component);
        renderNode(component._render(), true, context);
    }
}

/*  */

function createRenderer$1 (ref) {
    if ( ref === void 0 ) ref = {};
    var modules = ref.modules; if ( modules === void 0 ) modules = [];
    var directives = ref.directives; if ( directives === void 0 ) directives = {};
    var isUnaryTag = ref.isUnaryTag; if ( isUnaryTag === void 0 ) isUnaryTag = (function () { return false; });
    var template = ref.template;
    var inject = ref.inject;
    var cache = ref.cache;
    var shouldPreload = ref.shouldPreload;
    var clientManifest = ref.clientManifest;

    var render = createRenderFunction(modules, directives, isUnaryTag, cache);
    var templateRenderer = new TemplateRenderer({
        template: template,
        inject: inject,
        shouldPreload: shouldPreload,
        clientManifest: clientManifest
    });

    return {
        renderToString: function renderToString (
            component,
            context,
            done
        ) {
            if (typeof context === 'function') {
                done = context;
                context = {};
            }
            if (context) {
                templateRenderer.bindRenderFns(context);
            }
            var result = '';
            var write = createWriteFunction(function (text) {
                result += text;
                return false
            }, done);
            try {
                render(component, write, context, function () {
                    if (template) {
                        result = templateRenderer.renderSync(result, context);
                    }
                    done(null, result);
                });
            } catch (e) {
                done(e);
            }
        },

        renderToStream: function renderToStream (
            component,
            context
        ) {
            if (context) {
                templateRenderer.bindRenderFns(context);
            }
            var renderStream = new RenderStream(function (write, done) {
                render(component, write, context, done);
            });
            if (!template) {
                return renderStream
            } else {
                var templateStream = templateRenderer.createStream(context);
                renderStream.on('error', function (err) {
                    templateStream.emit('error', err);
                });
                renderStream.pipe(templateStream);
                return templateStream
            }
        }
    }
}

/*  */

function renderAttrs (node) {
    var attrs = node.data.attrs;
    var res = '';

    var parent = node.parent;
    while (isDef(parent)) {
        if (isDef(parent.data) && isDef(parent.data.attrs)) {
            attrs = Object.assign({}, attrs, parent.data.attrs);
        }
        parent = parent.parent;
    }

    if (isUndef(attrs)) {
        return res
    }

    for (var key in attrs) {
        if (key === 'style') {
            // leave it to the style module
            continue
        }
        res += renderAttr(key, attrs[key]);
    }
    return res
}

function renderAttr (key, value) {
    if (isBooleanAttr(key)) {
        if (!isFalsyAttrValue(value)) {
            return (" " + key + "=\"" + key + "\"")
        }
    } else if (isEnumeratedAttr(key)) {
        return (" " + key + "=\"" + (isFalsyAttrValue(value) || value === 'false' ? 'false' : 'true') + "\"")
    } else if (!isFalsyAttrValue(value)) {
        return (" " + key + "=\"" + (typeof value === 'string' ? he_2(value) : value) + "\"")
    }
    return ''
}

/*  */

var isAttr = makeMap(
    'accept,accept-charset,accesskey,action,align,alt,async,autocomplete,' +
    'autofocus,autoplay,autosave,bgcolor,border,buffered,challenge,charset,' +
    'checked,cite,class,code,codebase,color,cols,colspan,content,http-equiv,' +
    'name,contenteditable,contextmenu,controls,coords,data,datetime,default,' +
    'defer,dir,dirname,disabled,download,draggable,dropzone,enctype,method,for,' +
    'form,formaction,headers,height,hidden,high,href,hreflang,http-equiv,' +
    'icon,id,ismap,itemprop,keytype,kind,label,lang,language,list,loop,low,' +
    'manifest,max,maxlength,media,method,GET,POST,min,multiple,email,file,' +
    'muted,name,novalidate,open,optimum,pattern,ping,placeholder,poster,' +
    'preload,radiogroup,readonly,rel,required,reversed,rows,rowspan,sandbox,' +
    'scope,scoped,seamless,selected,shape,size,type,text,password,sizes,span,' +
    'spellcheck,src,srcdoc,srclang,srcset,start,step,style,summary,tabindex,' +
    'target,title,type,usemap,value,width,wrap'
);

/* istanbul ignore next */
var isRenderableAttr = function (name) {
    return (
        isAttr(name) ||
        name.indexOf('data-') === 0 ||
        name.indexOf('aria-') === 0
    )
};
var propsToAttrMap = {
    acceptCharset: 'accept-charset',
    className: 'class',
    htmlFor: 'for',
    httpEquiv: 'http-equiv'
};

/*  */

function renderDOMProps (node) {
    var props = node.data.domProps;
    var res = '';

    var parent = node.parent;
    while (isDef(parent)) {
        if (parent.data && parent.data.domProps) {
            props = Object.assign({}, props, parent.data.domProps);
        }
        parent = parent.parent;
    }

    if (isUndef(props)) {
        return res
    }

    var attrs = node.data.attrs;
    for (var key in props) {
        if (key === 'innerHTML') {
            setText(node, props[key], true);
        } else if (key === 'textContent') {
            setText(node, props[key], false);
        } else {
            var attr = propsToAttrMap[key] || key.toLowerCase();
            if (isRenderableAttr(attr) &&
                // avoid rendering double-bound props/attrs twice
                !(isDef(attrs) && isDef(attrs[attr]))) {
                res += renderAttr(attr, props[key]);
            }
        }
    }
    return res
}

function setText (node, text, raw) {
    var child = new VNode(undefined, undefined, undefined, text);
    child.raw = raw;
    node.children = [child];
}

/*  */

function renderClass (node) {
    var classList = genClassForVnode(node);
    if (classList !== '') {
        return (" class=\"" + (he_2(classList)) + "\"")
    }
}

/*  */

function genStyleText (vnode) {
    var styleText = '';
    var style = getStyle(vnode, false);
    for (var key in style) {
        var value = style[key];
        var hyphenatedKey = hyphenate(key);
        if (Array.isArray(value)) {
            for (var i = 0, len = value.length; i < len; i++) {
                styleText += hyphenatedKey + ":" + (value[i]) + ";";
            }
        } else {
            styleText += hyphenatedKey + ":" + value + ";";
        }
    }
    return styleText
}

function renderStyle (vnode) {
    var styleText = genStyleText(vnode);
    if (styleText !== '') {
        return (" style=" + (JSON.stringify(he_2(styleText))))
    }
}

var modules$2 = [
    renderAttrs,
    renderDOMProps,
    renderClass,
    renderStyle
];

/*  */

function show$1 (node, dir) {
    if (!dir.value) {
        var style = node.data.style || (node.data.style = {});
        style.display = 'none';
    }
}

var baseDirectives$1 = {
    show: show$1
};

/*  */

process.env.VUE_ENV = 'server';

// import { createBundleRendererCreator } from 'server/bundle-renderer/create-bundle-renderer'
function createRenderer$$1 (options) {
    if ( options === void 0 ) options = {};

    var renderer = createRenderer$1(Object.assign({}, options, {
        isUnaryTag: isUnaryTag,
        canBeLeftOpenTag: canBeLeftOpenTag,
        modules: modules$2,
        // user can provide server-side implementations for custom directives
        // when creating the renderer.
        directives: Object.assign(baseDirectives$1, options.directives)
    }));

    return {
        renderToString: function (component, done, context) {
            var results = awaitServer(function (complete) {
                renderer.renderToString(component, function (err, res) {
                    complete(err, res);
                }, context);
            });

            done(results.error, results.result);
            return results
        },
        renderToStream: function (component, context) {
            return renderer.renderToStream(component, context)
        }
    }
}



// export const createBundleRenderer = createBundleRendererCreator(createRenderer)

exports.Vue = Vue$3;
exports.createRenderer = createRenderer$$1;

global.Vue = Vue;

Vue;