Project 1 - Line segment intersection

The goal of the project is to be able to input line segments from the screen and a file, compute their intersections and to store them together with their intersections as a planar graph represented in a DCEL structure. A technical goal is to be able to handle DCEL's.

The running program should support the following operations.

Be able to read a file containing a sequence of line segments. Each line should have the format:
> x1 y1 x2 y2
where (x1,y1) and (x2,y2) are the endpoints of the segment. Coordiantes are floating point numbers.
Adding new line segments interactively by using the mouse.
Intersections between segments are to be reported and visualized by small circles.
Save the computed DCEL in a file.
Restore a DCEL from a file.
A running program implementing the sweep line algorithm in [BKOS, Chapter 2] and a report is expected.

Cf. http://www.cs.au.dk/~gerth/cg10/project.html