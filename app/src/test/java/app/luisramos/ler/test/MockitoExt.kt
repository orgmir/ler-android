package app.luisramos.ler.test

import org.mockito.Mockito.`when`
import org.mockito.stubbing.OngoingStubbing

fun <T : Any> whenever(methodCall: T): OngoingStubbing<T> = `when`(methodCall)