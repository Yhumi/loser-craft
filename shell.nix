{ pkgs ? import <nixpkgs> {} }:
  pkgs.mkShell {
    packages = [
      pkgs.jdk21
    ];

    shellHook = ''
      export LD_LIBRARY_PATH="''${LD_LIBRARY_PATH}''${LD_LIBRARY_PATH:+:}${pkgs.libglvnd}/lib"
      export JAVA_HOME="${pkgs.jdk21}/lib/openjdk"
      PATH="${pkgs.jdk21}/lib/bin:$PATH"
    '';
}