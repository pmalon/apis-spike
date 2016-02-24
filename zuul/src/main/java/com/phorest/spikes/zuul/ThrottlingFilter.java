package com.phorest.spikes.zuul;

import com.github.bucket4j.Bucket;
import com.github.bucket4j.Buckets;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Component
public class ThrottlingFilter extends ZuulFilter {

  private static final Logger LOG = LoggerFactory.getLogger(ThrottlingFilter.class);

  private UrlPathHelper urlPathHelper = new UrlPathHelper();

  @Autowired
  private RouteLocator routeLocator;

  Bucket bucket = Buckets.withNanoTimePrecision()
    .withLimitedBandwidth(5, TimeUnit.MINUTES, 1)
    .build();

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 0;
  }

  @Override
  public boolean shouldFilter() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    final String requestURI = this.urlPathHelper.getPathWithinApplication(request);
    Route route = this.routeLocator.getMatchingRoute(requestURI);
    LOG.info("Request path: {}, location: {}", route.getPath(), route.getLocation());
    return true;
  }


  @Override
  public Object run() {
    if (!bucket.tryConsumeSingleToken()) {
      setFailedRequest("too many requests", HttpStatus.TOO_MANY_REQUESTS.value());
      LOG.info("Too many requests");
    } else {
      LOG.info("Bucket state: {}", bucket.createSnapshot().createSnapshot());
    }

    return null;
  }

  private void setFailedRequest(String body, int code) {
    RequestContext ctx = RequestContext.getCurrentContext();
    ctx.setResponseStatusCode(code);
    if (ctx.getResponseBody() == null) {
      ctx.setResponseBody(body);
      ctx.setSendZuulResponse(false);
      throw new RuntimeException("Code: " + code + ", " + body); //optional
    }
  }
}
