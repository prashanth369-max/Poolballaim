#pragma once
#include "../vector/Vector2D.h"

class ReflectionEngine {
public:
    static Vector2D reflect(const Vector2D& v, const Vector2D& normal);
};
