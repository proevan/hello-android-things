package com.proevan.helloandroidthings

import android.app.Activity
import android.os.Bundle
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManagerService

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {

    companion object {
        private const val GPIO_RED_LED = "IO2"
        private const val GPIO_GREEN_LED = "IO3"
        private const val GPIO_BUTTON = "IO8"
    }

    private lateinit var pmService: PeripheralManagerService
    private lateinit var redLeoGpio: Gpio
    private lateinit var greenLedGpio: Gpio
    private lateinit var buttonGpio: Gpio
    private var redLedState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pmService = PeripheralManagerService()
        initIo()
    }

    private fun initIo() {
        initRedLed()
        initGreenLed()
        initButton()
    }

    private fun initRedLed() {
        redLeoGpio = pmService.openGpio(GPIO_RED_LED).apply {
            setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        }
    }

    private fun initGreenLed() {
        greenLedGpio = pmService.openGpio(GPIO_GREEN_LED).apply {
            setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        }
    }

    private fun initButton() {
        buttonGpio = pmService.openGpio(GPIO_BUTTON).apply {
            setDirection(Gpio.DIRECTION_IN)
            setEdgeTriggerType(Gpio.EDGE_BOTH)
            registerGpioCallback(object : GpioCallback() {
                override fun onGpioEdge(gpio: Gpio): Boolean {
                    greenLedGpio.value = gpio.value
                    if (gpio.value == false) {
                        redLedState = !redLedState
                        redLeoGpio.value = redLedState
                    }
                    return true
                }
            })
        }
    }

}
