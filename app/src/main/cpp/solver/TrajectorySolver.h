#pragma once
#include <vector>
#include "../vector/Vector2D.h"

struct TableInput {
    float left;
    float right;
    float top;
    float bottom;
};

class TrajectorySolver {
public:
    static std::vector<Vector2D> solve(
        const TableInput& table,
        const Vector2D& origin,
        const Vector2D& direction,
        int reflectionCount);
};
