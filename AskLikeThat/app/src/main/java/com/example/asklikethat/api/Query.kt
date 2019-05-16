package com.example.asklikethat.api

class Query {
    private val map: MutableMap<String, String> = mutableMapOf()

    fun setAmount(amount: Int): Query {
        map["amount"] = amount.toString()
        return this
    }

    fun setCategory(category: String): Query {
        map["category"] = category
        return this
    }

    fun setType(type: String): Query {
        map["type"] = type
        return this
    }

    fun setEncoding(encoding: String): Query {
        map["encoding"] = encoding
        return this
    }

    fun build() = map
}