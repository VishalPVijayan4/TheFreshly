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

### Home Screen
![Home](.screenshotss/Home%20Dashboard.png)

### Product Listing
![Products](./screenshotss/ProductListing.png)

### Product Details
![Details](./screenshotss/ProductDetails.png)

### Cart
![Cart](./screenshotss/Cart.png)

### Profile
![Profile](./screenshotss/Profile.png)

---

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
