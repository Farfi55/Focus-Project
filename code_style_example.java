// package at the top

/*
	varius imports
*/

// funcion description
// classes are singular nouns
// function name is the same as file name
// one Class per file, with the exception of nested classes
class ClassName 
{
	// space after comment
	
	
	/*
		TODO: multi line get indented
	*/
	
	// singleton at the top

	private static instance = new ClassName();
	public static getInstance() { return instance; }

	// variables at the top

	// variables have nouns
	int variableName;
	
	// variable description on top, or at the side of variable
	// but try to make it clear what variable does without using comments
	int variableName2; 
	
	bool isAlive;
	bool hasFinished;

	// use wrappers inside collections
	List<Integer> pluralNoun;


	/*
		FUNCIONS ORDER:
		1. getInstance() 
		2. constructors
		3. framework methods (@FXMl initialize(),  onKeyPressed(), etc... )
		4. public methods 
		5. private methods (if closly related can be close to related public methods) 
		6. getter & setters
	*/


	// constructor first
	public ClassName()
	{
		// var used only when the type is clear and is seen on the same line
		var a = new Integer(5);

		// borderline acceptable when function makes it clear what type it's returning 
		var b = Factory.buildNewPerson();

		// specify in all other cases
		Person c = Database.get(0);
	}

	// function description
	// functions have verbs
	public int functionName()
	{
		// tabs for indentation
		if (condition1)
		{			
			// ...
		}
		else if ()
		{
			// ...
		} 
		else 
		{
			// ...
		}

		// outer if must have brakets
		if (outerCondition && otherCondition) 
		{
			if (nested_condition) 
				singleIfInstruction
			else 
				singleElseInstruction
		}


		String title = switch (person) 
		{
			case Dali, Picasso -> "painter";
			case Mozart, Prokofiev -> "composer";
			case Goethe, Dostoevsky -> "writer";
		};		
		
	}


	// getter and setters at the end of file

	void setVar(int param) { variableName = param; } 	
	
	int getVar() { return variableName; } 
}
