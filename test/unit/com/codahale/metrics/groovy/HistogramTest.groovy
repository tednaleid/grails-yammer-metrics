package com.codahale.metrics.groovy

import com.codahale.metrics.Histogram
import com.codahale.metrics.SlidingTimeWindowReservoir
import org.grails.plugins.metrics.groovy.Metrics

import org.junit.Test

import java.util.concurrent.TimeUnit

class HistogramTest extends GroovyTestCase {

    @Test
    void testNewHistogramWithDefaultReservoir() {
        def histogram = Metrics.newHistogram("MyHistogram")
        assert histogram instanceof Histogram
    }

    @Test
    void testNewHistogramWithCustomReservoir() {
        Histogram histogram = Metrics.newHistogram("SlidingTimeWindowHistogram", new SlidingTimeWindowReservoir(100, TimeUnit.MILLISECONDS))
        assert histogram instanceof Histogram

        histogram.update(3)
        histogram.update(2)
        histogram.update(1)

        assert histogram.getSnapshot().size() == 3

        Thread.sleep(200)
        assert histogram.getSnapshot().size() == 0
    }
}
