package project.gizka.exception.notFound;

public class AppUserNotFoundException extends RuntimeException{

    public AppUserNotFoundException(String message){
        super(message);
    }
}
