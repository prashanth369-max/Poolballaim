cmake_minimum_required(VERSION 3.22.1)
project(poolaimassistant)

add_library(
        native-lib
        SHARED
        cpp/native-lib.cpp
        cpp/vector/Vector2D.cpp
        cpp/ghost/GhostBall.cpp
        cpp/reflection/ReflectionEngine.cpp
        cpp/mirror/MirrorTable.cpp
        cpp/solver/TrajectorySolver.cpp)

find_library(log-lib log)

target_include_directories(native-lib PRIVATE cpp)

target_link_libraries(native-lib ${log-lib})
