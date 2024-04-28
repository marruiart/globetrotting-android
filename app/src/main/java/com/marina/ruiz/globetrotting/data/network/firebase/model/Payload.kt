package com.marina.ruiz.globetrotting.data.network.firebase.model

class Payload {

    companion object {
        fun generateId(): String {
            val firstPart = (Math.random() * 1000000000000000).toLong().toString(36)
            val secondPart = (Math.random() * 1000000000000000).toLong().toString(36)
            return firstPart.substring(0, firstPart.length) + secondPart.substring(0, secondPart.length)
        }
    }

}