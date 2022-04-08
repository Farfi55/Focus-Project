module mafiadelprimobanco.focusproject {
	requires javafx.controls;
	requires javafx.fxml;
	
	requires org.kordamp.ikonli.javafx;
	
	opens mafiadelprimobanco.focusproject to javafx.fxml;
	exports mafiadelprimobanco.focusproject;
}