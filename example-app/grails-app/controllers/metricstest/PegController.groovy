package metricstest

import com.codahale.metrics.Counter
import com.codahale.metrics.Gauge
import com.codahale.metrics.Histogram
import org.grails.plugins.metrics.groovy.Metered
import org.grails.plugins.metrics.groovy.Metrics
import org.grails.plugins.metrics.groovy.Timed

class PegController {

    class DepthGauge implements Gauge<Long> {
        Long depth = 100

        Long getValue() {
            depth += 1
            return depth
        }
    }

    Counter counter = Metrics.newCounter("count.something")
    Histogram histogram = Metrics.newHistogram("sample.histogram")

    PegController() {
        Metrics.newGauge("DepthGauge", new DepthGauge())
    }

    @Metered( name = "meter" )
    def metered() {
        render( contentType: "text/plain", text: "Metered!" )
    }

    @Timed( name = "timer" )
    def timed(Integer id) {
        Integer millis = id ?: 200
        Thread.sleep(millis)
        render( contentType: "text/plain", text: "Timed for $millis!" )
    }

    def counter() {
        counter.inc()
        render( contentType: "text/plain", text: "Counted!" )
    }

    // pass in a different value to update the histogram
    def histogram(Integer id) {
        Integer value = id ?: 1
        histogram.update(value)
        render( contentType: "text/plain", text: "Histogram updated with $value!" )
    }

}
