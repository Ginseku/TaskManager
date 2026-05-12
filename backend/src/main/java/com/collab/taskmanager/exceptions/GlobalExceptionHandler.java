package com.collab.taskmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handlerGeneral(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error!");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid email or password");
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handlerAccountNotFound(UserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> handlerAccountNotFound(UserAlreadyExistException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
    @ExceptionHandler(UserAlreadyAdminException.class)
    public ResponseEntity<String> handlerUserAlreadyAdmin(UserAlreadyAdminException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
    @ExceptionHandler(UserIsNotAdminException.class)
    public ResponseEntity<String> handlerUserIsNotAdmin(UserIsNotAdminException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<String> handlerUserIsNotAdmin(TeamNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    @ExceptionHandler(TeamMemberNotFoundException.class)
    public ResponseEntity<String> handlerUserIsNotAdmin(TeamMemberNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    @ExceptionHandler(UserNameAlreadyExistsException.class)
    public ResponseEntity<String> handlerUserIsNotAdmin(UserNameAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<String> handlerUserIsNotAdmin(ProjectNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    @ExceptionHandler(UserIsNotOwnerException.class)
    public ResponseEntity<String> handlerUserIsNotAdmin(UserIsNotOwnerException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
    @ExceptionHandler(UserIsNotTeamMemberException.class)
    public ResponseEntity<String> handlerUserIsNotAdmin(UserIsNotTeamMemberException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
}
