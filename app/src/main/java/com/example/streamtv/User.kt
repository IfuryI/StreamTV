package com.example.streamtv

import java.io.Serializable

class User : Serializable {
    public var login: String = ""
    public var email: String = ""

    constructor() {}

    constructor(login: String, email: String) {
        this.login = login
        this.email = email
    }
}
