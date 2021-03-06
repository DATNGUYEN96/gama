/**
 * Purpose: Test that species must be visible to its sub-species.
 * 
 * Action(s):
 * 		1. Right click the mouse in the editor to parse and compile the model.
 * 
 * Expected outcome: 
 * 		1. Click on the "No experiment available (select to see errors)" menu.
 * 			The error message indicates that C species is not visible to D species.
 */model testcase39

import "platform:/plugin/msi.gama.application/generated/std.gaml"

global {
}

entities {
	species A skills: situated {
		species B {
			species C {
				
			}

		}
		
		species D parent: C {
			
		}
	}
	
	
}

environment width: 100 height: 100 {
}

experiment default_expr type: gui {
	
}

