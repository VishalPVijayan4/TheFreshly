package com.vishalpvijayan.thefreshly.data.remote.model.UserDetail

data class UserDetailDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val maidenName: String,
    val age: Int,
    val gender: String,
    val email: String,
    val phone: String,
    val username: String,
    val birthDate: String,
    val image: String,
    val bloodGroup: String,
    val height: Double,
    val weight: Double,
    val eyeColor: String,
    val hair: HairDto,
    val ip: String,
    val address: AddressDto,
    val macAddress: String,
    val university: String,
    val bank: BankDto,
    val company: CompanyDto,
    val ein: String,
    val ssn: String,
    val userAgent: String,
    val crypto: CryptoDto,
    val role: String
)

data class HairDto(val color: String, val type: String)
data class AddressDto(
    val address: String,
    val city: String,
    val state: String,
    val stateCode: String,
    val postalCode: String,
    val coordinates: CoordinatesDto,
    val country: String
)
data class CoordinatesDto(val lat: Double, val lng: Double)
data class BankDto(
    val cardExpire: String,
    val cardNumber: String,
    val cardType: String,
    val currency: String,
    val iban: String
)
data class CompanyDto(
    val department: String,
    val name: String,
    val title: String,
    val address: AddressDto
)
data class CryptoDto(
    val coin: String,
    val wallet: String,
    val network: String
)

