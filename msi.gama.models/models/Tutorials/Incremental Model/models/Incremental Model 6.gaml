/**
 *  model6
 *  This model illustrates multi-level modeling
 */
model model6 
 
global  {
	int nb_people <- 500;
	float agent_speed <- 5.0 #km/#h;
	float step <- 1 #minutes;
	float infection_distance <- 2.0 #m;
	float proba_infection <- 0.05;
	int nb_infected_init <- 5;
	file roads_shapefile <- file("../includes/road.shp");
	file buildings_shapefile <- file("../includes/building.shp");
	geometry shape <- envelope(roads_shapefile);
	graph road_network;
	int current_hour update: (cycle / 60) mod 24;
	float staying_coeff update: 10.0 ^ (1 + min([abs(current_hour - 9), abs(current_hour - 12), abs(current_hour - 18)]));
	
	list<people_in_building> list_people_in_buildings update: (building accumulate each.people_inside) where (not dead(each));
	int nb_people_infected <- nb_infected_init update: people count (each.is_infected) + (empty(list_people_in_buildings) ? 0 : list_people_in_buildings count (each.is_infected));
	
	int nb_people_not_infected <- nb_people - nb_infected_init update: nb_people - nb_people_infected;
	bool is_night <- true update: current_hour < 7 or current_hour > 20;
	
	float infected_rate update: nb_people_infected/nb_people;
	init {
		create road from: roads_shapefile;
		road_network <- as_edge_graph(road);
		create building from: buildings_shapefile;
		create people number:nb_people {
			building bd <- one_of(building);
			location <- any_location_in(bd);
		}
		ask nb_infected_init among people {
			is_infected <- true;
		}
		
	}
	reflex end_simulation when: infected_rate = 1.0 {		
		do halt;
	}
}

species people skills:[moving]{		
	float speed <- agent_speed;
	bool is_infected <- false;
	point target;
	int staying_counter;
		
	reflex move when: target != nil{
		do goto target:target on: road_network;
		if (location = target) {
			target <- any_location_in (one_of(building));
			target <- nil;
			staying_counter <- 0;
		} 
	}
	reflex infect when: is_infected{
		ask people at_distance infection_distance {
			if flip(proba_infection) {
				is_infected <- true;
			}
		}
	}
	aspect circle{
		draw circle(5) color:is_infected ? #red : #green;
	}
	aspect sphere3D{
		draw sphere(5) color:is_infected ? #red : #green;
	}
}

species road {
	geometry display_shape <- shape + 2.0;
	aspect geom {
		draw display_shape color: #black depth: 3.0;
	}
}

species building {
	float height <- 10#m + rnd(10) #m;
	list<people_in_building> people_inside -> {members collect people_in_building(each)};
	
	aspect geom {
		int nbI <- members count people_in_building(each).is_infected;
		int nbT <- length(members);
		draw shape color:nbT = 0 ? #gray : (float(nbI)/nbT > 0.5 ? #red : #green) depth: height;
	}
	
	species people_in_building parent: people schedules: [] {
		aspect circle{}
		aspect sphere3D{}
	}
	
	species people_in_2 parent: people schedules: [] {
		aspect circle{}
		aspect sphere3D{}
	}
	
	reflex let_people_leave  {
		ask members as: people_in_building{
			staying_counter <- staying_counter + 1;
		}
		list<people_in_building> leaving_people <- list<people_in_building>(members where (flip(people_in_building(each).staying_counter / staying_coeff)));
		if not (empty (leaving_people)) {
			release leaving_people as: people in: world returns: released_people;
			ask released_people {
				target <- any_location_in (one_of(building));
			}
		}
	}
	reflex let_people_enter {
		list<people> entering_people <- people inside self where (each.target = nil);
		if not (empty (entering_people)) {
			capture entering_people as: people_in_building ;
 		}
	}
}

experiment main_experiment type:gui{
	parameter "Infection distance" var: infection_distance;
	parameter "Proba infection" var: proba_infection min: 0.0 max: 1.0;
	parameter "Nb people infected at init" var: nb_infected_init ;
	output {
		monitor "Current hour" value: current_hour;
		monitor "Infected people rate" value: infected_rate;
		display map_3D type: opengl ambient_light: is_night ? 30 : 120{
			image "../includes/soil.jpg";
			species road aspect:geom;
			species building aspect:geom;
			species people aspect:sphere3D;			
		}
		display chart refresh_every: 10 {
			chart "Disease spreading" type: series {
				data "susceptible" value: nb_people_not_infected color: #green;
				data "infected" value: nb_people_infected color: #red;
			}
		}
	}
}