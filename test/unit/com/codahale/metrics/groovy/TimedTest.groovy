package com.codahale.metrics.groovy

import com.codahale.metrics.Timer
import org.grails.plugins.metrics.groovy.Metrics

import org.junit.Test
import org.junit.Before
import org.junit.After


class TimedTest extends GroovyTestCase {
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
    void testAddingTimerField(){
        def timer = sample.hasProperty('timedTimer') ? sample.timedTimer : null
        assertNotNull("We should generate 'timedTimer' for timed() :> ${sample.dump()}", timer)
        if(!timer instanceof Timer){
            fail("We should generate a Timer for timed()  :> ${sample.dump()}")
        }
    }

    @Test
    void testNotAddingTimerField(){
        def timer = sample.hasProperty('notTimedTimer')?sample.notTimedTimer : null
        assertNull("We should NOT generate 'notTimedTimer' for notTimed() :> ${sample.dump()}", timer)
    }

    @Test
    void testCreatingNamedTimer(){
        def defaultASTTimer = sample.hasProperty('timedWithNamedTimer') ? sample.timedWithNamedTimer : null
        def namedTimer = sample.hasProperty('namedTimerInst') ? sample.namedTimerInst : null
        assertNull("We should NOT have created a default timer via AST when the annotation provides a name", defaultASTTimer)
        assertNotNull("We should have created a timer via AST to match the annotations name param", namedTimer)
    }

    @Test
    void testUsingNamedTimer(){
        def namedTimer = sample.namedTimerInst
        def callCount = 10
        callCount.times{
            sample.timedWithNamed()
        }
        assertEquals("We should see ${callCount} calls on our timer", callCount, namedTimer.count )
    }

    @Test
    void testSetupExistingNamedTimer(){
        def defaultASTTimer = sample.hasProperty('timedWithExistingNamedTimer') ? sample.timedWithExistingNamedTimer : null
        assertNull("We should NOT have created a default timer via AST when the annotation provides a name to an existing timer", defaultASTTimer)
    }

    @Test
    void testUsingExistingNamedTimer(){
        def namedExistingTimer = sample.existingNamedTimerInst
        def callCount = 10
        callCount.times{
            sample.timedWithExistingNamed()
        }
        assertEquals("We should see ${callCount} calls on our timer", callCount, namedExistingTimer.count )

    }

    //Not going crazy testing the yammer code, just want to ensure that our AST is functioning.
    @Test
    void testCallingTimedMethod(){
        def callCount = 10
        callCount.times {
            sample.timed()
        }
        def timer = sample.timedTimer
        assertEquals("We should see ${callCount} calls on our timer", callCount, timer.count )

    }


    //Not going crazy testing the yammer code, just want to ensure that our AST is functioning.
    @Test
    void testCallingMultiMetricMethod(){
        def timer = sample.multiMetricTimer
assertEquals( 0, timer.count )
        def callCount = 10
        callCount.times {
            sample.multiMetric()
        }
        assertEquals("We should see ${callCount} calls on our timer", callCount, timer.count )
    }
}
