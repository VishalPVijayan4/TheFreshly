package com.vishalpvijayan.thefreshly.data.mapper

import com.vishalpvijayan.thefreshly.data.remote.model.DtoModel.DimensionsDto
import com.vishalpvijayan.thefreshly.data.remote.model.DtoModel.MetaDto
import com.vishalpvijayan.thefreshly.data.remote.model.DtoModel.ProductDetailDto
import com.vishalpvijayan.thefreshly.data.remote.model.DtoModel.ReviewDto
import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.Dimensions
import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.Meta
import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.ProductDetail
import com.vishalpvijayan.thefreshly.data.remote.model.productDetail.Review


fun ProductDetailDto.toDomain(): ProductDetail = ProductDetail(
    id, title, description, category, price, discountPercentage, rating,
    stock, tags, brand, sku, weight,
    dimensions = dimensions.toDomain(),
    warrantyInformation, shippingInformation, availabilityStatus,
    reviews = reviews.map { it.toDomain() },
    returnPolicy, minimumOrderQuantity,
    meta = meta.toDomain(),
    images, thumbnail
)

fun DimensionsDto.toDomain(): Dimensions = Dimensions(width, height, depth)
fun ReviewDto.toDomain(): Review = Review(rating, comment, date, reviewerName, reviewerEmail)
fun MetaDto.toDomain(): Meta = Meta(createdAt, updatedAt, barcode, qrCode)