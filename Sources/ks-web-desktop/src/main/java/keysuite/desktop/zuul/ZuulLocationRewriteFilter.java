/*
 * Copyright 2017-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.netflix.zuul.filters.post;

import java.net.URI;

import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;

/**
 * {@link ZuulFilter} Responsible for rewriting the Location header to be the Zuul URL.
 *
 * @author Biju Kunjummen
 */
public class ZuulLocationRewriteFilter extends LocationRewriteFilter {

    public ZuulLocationRewriteFilter() {
    }

    public ZuulLocationRewriteFilter(ZuulProperties zuulProperties,
                                 RouteLocator routeLocator) {
        super(zuulProperties,routeLocator);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        if (ctx.getZuulResponseHeaders() != null) {
            for (Pair<String, String> pair : ctx.getZuulResponseHeaders()) {
                if (pair.first().equalsIgnoreCase("Location")) {
                    String dest = ctx.getRouteHost().toString().toLowerCase();
                    if (pair.second().toLowerCase().startsWith(dest)){
                        super.run();
                    }
                }
            }
        }
        return null;
    }
}
