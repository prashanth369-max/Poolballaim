#include "MirrorTable.h"

Vector2D MirrorTable::mirrorAcrossVertical(const Vector2D& point, float axisX) {
    return {2.0f * axisX - point.x, point.y};
}

Vector2D MirrorTable::mirrorAcrossHorizontal(const Vector2D& point, float axisY) {
    return {point.x, 2.0f * axisY - point.y};
}
