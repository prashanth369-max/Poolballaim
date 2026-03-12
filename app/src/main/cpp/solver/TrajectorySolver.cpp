#include "TrajectorySolver.h"
#include "../reflection/ReflectionEngine.h"

std::vector<Vector2D> TrajectorySolver::solve(
    const TableInput& table,
    const Vector2D& origin,
    const Vector2D& direction,
    int reflectionCount
) {
    std::vector<Vector2D> points;
    Vector2D current = origin;
    Vector2D dir = direction.normalized();
    points.push_back(current);

    for (int i = 0; i < reflectionCount + 1; ++i) {
        Vector2D next = current + dir * 500.0f;
        if (next.x < table.left || next.x > table.right) {
            dir = ReflectionEngine::reflect(dir, {1.0f, 0.0f});
            next = current + dir * 500.0f;
        }
        if (next.y < table.top || next.y > table.bottom) {
            dir = ReflectionEngine::reflect(dir, {0.0f, 1.0f});
            next = current + dir * 500.0f;
        }
        points.push_back(next);
        current = next;
    }
    return points;
}
