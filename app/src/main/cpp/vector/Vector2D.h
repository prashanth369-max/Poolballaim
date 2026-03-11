#pragma once

struct Vector2D {
    float x;
    float y;

    Vector2D();
    Vector2D(float x, float y);

    Vector2D operator+(const Vector2D& other) const;
    Vector2D operator-(const Vector2D& other) const;
    Vector2D operator*(float scalar) const;
    float length() const;
    Vector2D normalized() const;
};
