#include "ReflectionEngine.h"

static float dot(const Vector2D& a, const Vector2D& b) { return a.x * b.x + a.y * b.y; }

Vector2D ReflectionEngine::reflect(const Vector2D& v, const Vector2D& normal) {
    Vector2D n = normal.normalized();
    float projection = dot(v, n);
    return v - n * (2.0f * projection);
}
