package com.codahale.metrics.groovy

import com.codahale.metrics.Meter
import org.grails.plugins.metrics.groovy.Metrics

import org.junit.Test
import org.junit.Before
import org.junit.After


class MeterTest extends GroovyTestCase {
    SampleObject sample

    @Before
    void setUp(){
        sample = new SampleObject()
    }

    @After
    void tearDown() {
        Metrics.removeAll()
    }

    @Test
    void testAddingMeterField(){
        def meter = sample.hasProperty('meteredMeter')?sample.meteredMeter : null
        assertNotNull("We should generate 'meteredMeter' for metered() :> ${sample.dump()}", meter)
        if(!meter instanceof Meter){
            fail("We should generate a com.codahale.metrics.meter for timed()  :> ${sample.dump()}")
        }

    }

    @Test
    void testNotAddingMeterField(){
        def meter = sample.hasProperty('notMeteredMeter')?sample.notMeteredMeter : null
        assertNull("We should NOT generate 'notMeteredMeter' for notMetered() :> ${sample.dump()}", meter)
    }

    //Not going crazy testing the yammer code, just want to ensure that our AST is functioning.
    @Test
    void testCallingMeteredMethod(){

        def init = sample.meteredMeter.count
        def callCount = 10
        callCount.times {
            sample.metered()
        }
        Meter meter = sample.meteredMeter
        assertEquals("We should see ${callCount+init} calls on our meter", callCount+init, meter.count )
    }

    @Test
    void testCountsSpanInstances(){
        def one = new SampleObject()
        def two = new SampleObject()
        def meterOne = one.anotherMeteredMeter
        def meterTwo = two.anotherMeteredMeter
        def initOne = meterOne.count
        def initTwo = meterTwo.count
        def callCount = 10

        10.times{
            one.anotherMetered()
            two.anotherMetered()
        }

        assertEquals("We should see ${callCount} calls on our meter", (callCount*2) + initOne, meterOne.count )
        assertEquals("We should see ${callCount} calls on our meter", (callCount*2) + initTwo, meterTwo.count )

    }

    //Not going crazy testing the yammer code, just want to ensure that our AST is functioning.
    @Test
    void testCallingMultiMetricMethod(){
        def init = sample.multiMetricMeter.count
        def callCount = 10
        callCount.times {
            sample.multiMetric()
        }
        Meter meter = sample.multiMetricMeter
        assertEquals("We should see ${callCount + init} calls on our meter", callCount + init, meter.count   )
    }
}
