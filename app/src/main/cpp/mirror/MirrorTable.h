#pragma once
#include "../vector/Vector2D.h"

class MirrorTable {
public:
    static Vector2D mirrorAcrossVertical(const Vector2D& point, float axisX);
    static Vector2D mirrorAcrossHorizontal(const Vector2D& point, float axisY);
};
