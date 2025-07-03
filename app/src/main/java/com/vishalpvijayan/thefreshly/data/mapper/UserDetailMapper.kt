package com.vishalpvijayan.thefreshly.data.mapper

import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.Address
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.AddressDto
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.Bank
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.BankDto
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.Company
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.CompanyDto
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.Coordinates
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.CoordinatesDto
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.Crypto
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.CryptoDto
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.Hair
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.HairDto
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.UserDetail
import com.vishalpvijayan.thefreshly.data.remote.model.UserDetail.UserDetailDto

fun UserDetailDto.toDomain(): UserDetail = UserDetail(
    id, firstName, lastName, maidenName, age, gender, email, phone, username, birthDate,
    image, bloodGroup, height, weight, eyeColor,
    hair = hair.toDomain(),
    ip, address = address.toDomain(), macAddress, university,
    bank = bank.toDomain(), company = company.toDomain(),
    ein, ssn, userAgent, crypto = crypto.toDomain(), role
)

fun HairDto.toDomain() = Hair(color, type)
fun CoordinatesDto.toDomain() = Coordinates(lat, lng)
fun AddressDto.toDomain() = Address(address, city, state, stateCode, postalCode, coordinates.toDomain(), country)
fun BankDto.toDomain() = Bank(cardExpire, cardNumber, cardType, currency, iban)
fun CompanyDto.toDomain() = Company(department, name, title, address.toDomain())
fun CryptoDto.toDomain() = Crypto(coin, wallet, network)