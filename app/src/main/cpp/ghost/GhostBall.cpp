#include "GhostBall.h"

Vector2D GhostBall::computeGhostBall(const Vector2D& target, const Vector2D& direction, float ballRadius) {
    Vector2D d = direction.normalized();
    return target - (d * (2.0f * ballRadius));
}

Vector2D GhostBall::computeContactPoint(const Vector2D& target, const Vector2D& ghost) {
    Vector2D axis = (target - ghost).normalized();
    return target - axis;
}
