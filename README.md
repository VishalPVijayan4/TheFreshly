# 🥗 TheFreshly

A modern grocery and fresh produce e-commerce application built using Android best practices, delivering a seamless shopping experience with beautiful UI, product discovery, cart management, and secure checkout.

## ✨ Features

- Modern Material Design UI
- Product Catalog
- Category Browsing
- Search Functionality
- Product Details
- Shopping Cart
- Wishlist Support
- User Authentication
- Order Management
- Responsive Layouts
- Offline Support
- API Integration

---

## 📱 Screenshots

<p align="center">
  <img src="screenshotss/Home%20Dashboard.png" width="180">
  <img src="screenshotss/Live%20Tracking.png" width="180">
  <img src="screenshotss/Onboarding%20-%20Fast%20Delivery.png" width="180">
  <img src="screenshotss/Product%20Detail.png" width="180">
  <img src="screenshotss/Your%20Cart.png" width="180">
</p>


## 🏗️ Architecture

This project follows Clean Architecture principles combined with MVVM architecture to ensure:

- Scalability
- Maintainability
- Testability
- Separation of Concerns

```text
Presentation
│
├── UI (Compose)
├── ViewModel
│
Domain
│
├── Use Cases
├── Repository Contracts
│
Data
│
├── Repository Implementation
├── Remote Data Source
├── Local Data Source
