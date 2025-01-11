ReadMe.txt
– Hints on how program works:
	- Menu Screen: There are 2 buttons, one starts the program, and another displays a hint on how drag and drop works.
	
	- Prescription maker/main screen: A drug name from the list can be dragged onto the prescription side of the screen.
	   - Certain fields on the prescription side are required(validation for format and input is included).
	   - In the middle, the drug information will be displayed.
	   - The menu bar has more buttons that lead to different screens.
	   
	- Prescription search: Allows for user to search patients that have prescriptions.
	   - Contains a JTable to display all prescriptions a patient has.
	   
	- Patient search: Allows for user to search all patients, typing in the first 3 letters of the patient's name will auto-fill.
	
	- Patient report: Shows a list of all patients, for reference.
	
	- System summary report: Shows statistics, such as number of patients, number of prescriptions, credits, and current date.

– Any functionalities missing from your original plan for the game:
	- Did not have the promised minimum of 50 different drugs.

– Any additional functionalities added from your original plan:
	- If patients have prescriptions on hand, they cannot be removed from the system.
	
	- Added a menu bar, which includes the report, search, and Rx button.
	   - Rx button includes exit button and about button.
	   
	- Added a system summary/credits.
	   - Includes system runtime(how long the system has been running), number of patients, and prescriptions.
	   
	- Added a help button on the menu screen and the program screen.
	   - Indicates how drag and drop works.
	   
	- Auto-fill patient information by typing first 3 letters of patient name.
	
	- Validation for input, and format.
	   - The error messages displayed are in one dialog, originally, they would have been displayed properly, but due to time constrains, all of the error messages will be in one dialog.

– Known bugs / errors in your game:
	- No known bugs / all encountered bugs have been fixed.
	
– Any other important info for me to play/mark your game:
    - Prescription management system is very massive, there are many factors such as billing, inventory, patients, etc.
        - I have mainly focused on patients and prescription management.
        
    - Many of the functions are very similar, so I have used concepts we have learned in class.
        - Examples of concepts would be Comparator, Maps, Sets, Lists, StringBuilder, etc.
        
    - I have also learned new things such as implementing Serializable, JTables, drag and drop, and different EventListeners.
        - Serializable saves into a binary file, which makes it easier to read in the data, and makes it more efficient than text-file streaming. 
    