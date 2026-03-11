#include <jni.h>
#include <vector>
#include "ghost/GhostBall.h"
#include "solver/TrajectorySolver.h"

extern "C"
JNIEXPORT jfloatArray JNICALL
Java_com_pool_aim_geometry_GeometryBridge_computeTrajectoryNative(
    JNIEnv* env,
    jobject,
    jfloat tableLeft,
    jfloat tableRight,
    jfloat tableTop,
    jfloat tableBottom,
    jfloat ballRadius,
    jfloat redMarkerX,
    jfloat redMarkerY,
    jfloat whiteMarkerX,
    jfloat whiteMarkerY,
    jint reflectionCount
) {
    Vector2D red(redMarkerX, redMarkerY);
    Vector2D white(whiteMarkerX, whiteMarkerY);
    Vector2D direction = white - red;

    Vector2D ghost = GhostBall::computeGhostBall(red, direction, ballRadius);
    Vector2D contact = GhostBall::computeContactPoint(red, ghost);

    TableInput table{tableLeft, tableRight, tableTop, tableBottom};
    std::vector<Vector2D> trajectory = TrajectorySolver::solve(table, red, direction, reflectionCount);

    std::vector<float> packed;
    packed.push_back(ghost.x);
    packed.push_back(ghost.y);
    packed.push_back(contact.x);
    packed.push_back(contact.y);

    for (const auto& p : trajectory) {
        packed.push_back(p.x);
        packed.push_back(p.y);
    }

    jfloatArray result = env->NewFloatArray(static_cast<jsize>(packed.size()));
    env->SetFloatArrayRegion(result, 0, static_cast<jsize>(packed.size()), packed.data());
    return result;
}
