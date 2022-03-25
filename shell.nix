{ pkgs ? import <nixpkgs> {} }:
  pkgs.mkShell {

    nativeBuildInputs = [ pkgs.openjdk17
                          pkgs.maven 
                          pkgs.heroku
                          pkgs.redis
                        ]; 

}
