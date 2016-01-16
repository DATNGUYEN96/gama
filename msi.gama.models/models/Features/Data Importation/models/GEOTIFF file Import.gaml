/**
* Name: GeoTIFF file to Grid of Cells 
* Author:  Patrick Taillandier
* Description: Model which shows how to create a grid of cells by using a GeoTIFF File. 
* Tag :  Import Files, GIS
*/

model geotiffimport

global {
	//definiton of the file to import
	file grid_file <- file('../includes/bogota_grid.tif') ;
	
	//computation of the environment size from the geotiff file
	geometry shape <- envelope(grid_file);	
	
	float max_value;
	float min_value;
	init {
		max_value <- cell max_of (each.grid_value);
		min_value <- cell min_of (each.grid_value);
		ask cell {
			int val <- int(255 * ( 1  - (grid_value - min_value) /(max_value - min_value)));
			color <- rgb(val,val,val);
		}
	}
}

//definition of the grid from the geotiff file: the width and height of the grid are directly read from the asc file. The values of the asc file are stored in the grid_value attribute of the cells.
grid cell file: grid_file;

experiment show_example type: gui {
	output {
		display test {
			grid cell lines: #black;
		}
	} 
}