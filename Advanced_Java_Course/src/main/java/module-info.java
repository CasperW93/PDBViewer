module com.example.advanced_java_course {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.json;


    opens com.example.advanced_java_course.Assignment_3;
    exports com.example.advanced_java_course.Assignment_3;

    opens com.example.advanced_java_course.Assignment_3.model to javafx.fxml;
    exports com.example.advanced_java_course.Assignment_3.model;

    exports com.example.advanced_java_course.Assignment_3.window;
    opens com.example.advanced_java_course.Assignment_3.window to javafx.fxml;


    opens Assignment_4;
    exports Assignment_4;

    opens Assignment_4.model to javafx.fxml;
    exports Assignment_4.model;

    exports Assignment_4.window;
    opens Assignment_4.window to javafx.fxml;

    opens Assignment_5;
    exports Assignment_5;

    opens Assignment_5.model to javafx.fxml;
    exports Assignment_5.model;

    exports Assignment_5.window;
    opens Assignment_5.window to javafx.fxml;



    opens PDBExplorer;
    exports PDBExplorer;

    opens PDBExplorer.model to javafx.fxml;
    exports PDBExplorer.model;

    opens PDBExplorer.window to javafx.fxml;
    exports PDBExplorer.window;





}