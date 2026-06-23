package com.vishalpvijayan.thefreshly.data.mapper

import com.vishalpvijayan.thefreshly.data.remote.model.DtoModel.DimensionsDto
import com.vishalpvijayan.thefreshly.data.remote.model.DtoModel.MetaDto
import com.vishalpvijayan.thefreshly.data.remote.model.DtoModel.ProductDetailDto
import com.vishalpvijayan.thefreshly.data.remote.model.DtoModel.ReviewDto
import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.Dimensions
import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.Meta
import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.ProductDetail
import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.Review

private const val DESCRIPTION_NOT_AVAILABLE = "Description not available"
private const val NOT_AVAILABLE = "Not available"

fun ProductDetailDto.toDomain(): ProductDetail = ProductDetail(
    id = id ?: 0,
    title = title.orNotAvailable(),
    description = description.orDescriptionNotAvailable(),
    category = category.orNotAvailable(),
    price = price ?: 0.0,
    discountPercentage = discountPercentage ?: 0.0,
    rating = rating ?: 0.0,
    stock = stock ?: 0,
    tags = tags.orEmpty(),
    brand = brand.orDescriptionNotAvailable(),
    sku = sku.orNotAvailable(),
    weight = weight ?: 0,
    dimensions = dimensions.toDomain(),
    warrantyInformation = warrantyInformation.orNotAvailable(),
    shippingInformation = shippingInformation.orNotAvailable(),
    availabilityStatus = availabilityStatus.orNotAvailable(),
    reviews = reviews.orEmpty().map { it.toDomain() },
    returnPolicy = returnPolicy.orNotAvailable(),
    minimumOrderQuantity = minimumOrderQuantity ?: 0,
    meta = meta.toDomain(),
    images = images.orEmpty(),
    thumbnail = thumbnail.orEmpty()
)

fun DimensionsDto?.toDomain(): Dimensions = Dimensions(
    width = this?.width ?: 0.0,
    height = this?.height ?: 0.0,
    depth = this?.depth ?: 0.0
)

fun ReviewDto.toDomain(): Review = Review(
    rating = rating ?: 0,
    comment = comment.orDescriptionNotAvailable(),
    date = date.orNotAvailable(),
    reviewerName = reviewerName.orNotAvailable(),
    reviewerEmail = reviewerEmail.orNotAvailable()
)

fun MetaDto?.toDomain(): Meta = Meta(
    createdAt = this?.createdAt.orNotAvailable(),
    updatedAt = this?.updatedAt.orNotAvailable(),
    barcode = this?.barcode.orNotAvailable(),
    qrCode = this?.qrCode.orNotAvailable()
)

private fun String?.orDescriptionNotAvailable(): String =
    takeUnless { it.isNullOrBlank() } ?: DESCRIPTION_NOT_AVAILABLE

private fun String?.orNotAvailable(): String =
    takeUnless { it.isNullOrBlank() } ?: NOT_AVAILABLE
