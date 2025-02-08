# QuadTree-based Map System

A custom **map system** that efficiently manages and queries geospatial data using the **QuadTree** data structure. Designed to handle large-scale spatial data, the system enables **fast searching, insertion, deletion, and updates** of locations within a predefined 2D space.

## Tech Stack  

- Java  

## Features  

- **QuadTree-based Spatial Indexing**: Efficiently stores and retrieves points of interest (POI) by subdividing the map into quadrants.  
- **Point of Interest (POI) Management**: Supports adding, editing, and removing places with associated services.  
- **Optimized Geospatial Search**: Finds places within a user-defined boundary and filters results based on service type.  
- **Efficient Storage**: Uses an **array-based approach** within QuadTree nodes for better memory management.  
- **Dynamic Subdivision**: Automatically splits nodes when exceeding capacity for efficient query performance.  
- **Proximity-based Results**: Returns results sorted by Euclidean distance from the user’s location.  
- **Scalability**: Designed to handle up to **100 million places** within a **10,000,000 x 10,000,000** map size.  
- **Custom Data Structures**: Implemented without external libraries or Java’s built-in Collections framework.  
