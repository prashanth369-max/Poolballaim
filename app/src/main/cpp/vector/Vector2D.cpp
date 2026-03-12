#include "Vector2D.h"
#include <cmath>

Vector2D::Vector2D() : x(0), y(0) {}
Vector2D::Vector2D(float xVal, float yVal) : x(xVal), y(yVal) {}
Vector2D Vector2D::operator+(const Vector2D& other) const { return {x + other.x, y + other.y}; }
Vector2D Vector2D::operator-(const Vector2D& other) const { return {x - other.x, y - other.y}; }
Vector2D Vector2D::operator*(float scalar) const { return {x * scalar, y * scalar}; }
float Vector2D::length() const { return std::sqrt(x * x + y * y); }
Vector2D Vector2D::normalized() const {
    float len = length();
    return len <= 0.0001f ? Vector2D(0, 0) : Vector2D(x / len, y / len);
}
