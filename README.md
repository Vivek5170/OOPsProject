# Oops Project - 2D Floor Planner

## Overview
The **2D Floor Planner** is a Java Swing-based GUI application that allows users to design floor plans interactively. Users can add rooms, doors, windows, and furniture while ensuring proper placement through overlap checks. The project includes essential functionalities like saving and loading floor plans, grid snap, wall snap, relative room positioning, and drag-and-drop editing.

## Features
### Basic Room Creation
- Users can add rooms with predefined sizes and colors:
  - **Bedroom**: Green
  - **Bathroom**: Blue
  - **Kitchen**: Red
  - **Dining/Living Room**: Yellow/Orange
  - **Walls**: Black
  - **Outside Area**: Light Gray
- Rooms are placed in a **raster scanning manner** (row-major order).
- Overlapping rooms are not allowed.

### Advanced Room Placement
- Rooms can be added **relative to existing rooms** with:
  - **Directional Placement**: North, South, East, or West
  - **Alignment**: Left, Center, Right (for North/South), Top, Center, Bottom (for East/West)
- Overlap checks ensure correct room positioning.

### Room Deletion
- Rooms can be deleted by right clicking and selecting delete. (Usual/default)
- Trash Area Activation: Clicking the 'G' symbol (a circular button at the bottom of the control panel) enables the trash area. Rooms can be dragged onto it for deletion.

### Editing and Manipulation
- **Dragging Rooms**: Click and drag rooms within the canvas.
- **Overlap Validation**: If placement is invalid, an error message is shown, and the room returns to its original position.
- **Saving and Loading Plans**: Plans can be saved to a file and reloaded later.
- **Rotation of Furniture**: Furniture items can be rotated in **90-degree steps** (Can be used via right click menu).

### Doors, Windows, and Furniture
- **Doors**:
  - Placed between two rooms (represented as an opening in the wall).
  - Cannot be placed between bedrooms/bathrooms and the outside.
  - Open kitchens can have doorways equal to the wall length (Multiple doors placed side by side).
- **Windows**:
  - Represented by dashed lines.
  - Cannot be placed between two rooms.
- **Furniture and Fixtures**:
  - Items: Bed, Chair, Table, Sofa, Dining Set, Commode, Washbasin, Shower, Kitchen Sink, Stove.
  - Cannot overlap with rooms, doors, or windows.
  
## User Interface
### Control Panel (Left 25%)
- **Toolbar (Top of Control Panel)**: Contains `Save Plan` and `Load Plan` buttons.
- **Room and Furniture Buttons**: Allows users to add elements to the floor plan.
- **Configuration**:
  - Room selection: Users choose the type and size before placement.
  - Furniture selection: Users pick furniture before placing it.

### Canvas (Remaining 75%)
- **Displays the floor plan**: Shows rooms (color-coded), doors, windows, and furniture.
- **Trash Area (Top-Right)**: Dragging a room or furniture here removes it.
- **Grid Snapping**: Reference dots for better alignment.

## Installation and Usage
### Prerequisites
- Java 8 or higher
- Swing library (included in Java SE)

### Running the Application
1. Clone the repository:
   ```sh
   git clone https://github.com/Vivek5170/OOPsProject.git
   ```
2. Navigate to the project directory:
   ```sh
   cd yourfoldername
   ```
3. Compile and run the application:
   ```sh
   javac Frame.java
   java Frame
   ```

## Configuration
- The default room colors and dimensions can be modified in the source code (`room.java`).
- Furniture and fixtures are stored as PNG images in the `lib/lib/` folder.

## Disclaimer
- UI components are optimized for desktop use only (touchscreen support is included).

