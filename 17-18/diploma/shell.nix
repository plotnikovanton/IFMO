with import <nixpkgs> {};

(python36.withPackages (ps: [ps.pytorch ps.jupyter ps.numpy ps.sympy ps.matplotlib])).env
