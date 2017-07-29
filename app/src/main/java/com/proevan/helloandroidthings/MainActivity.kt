package com.proevan.helloandroidthings

import android.app.Activity
import android.os.Bundle
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManagerService

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
