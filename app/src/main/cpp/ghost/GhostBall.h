#pragma once
#include "../vector/Vector2D.h"

class GhostBall {
public:
    static Vector2D computeGhostBall(const Vector2D& target, const Vector2D& direction, float ballRadius);
    static Vector2D computeContactPoint(const Vector2D& target, const Vector2D& ghost);
};
