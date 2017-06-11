#!/bin/sh

path_acertos=testes/acertos/
path_erros=testes/erros/

# Todos os arquivos de teste
testes=($(ls $path_acertos*.in $path_erros*.in))

javac -classpath src/ src/Main.java

# if genC doesnt exist
if [ ! -d genC/ ]; then
	mkdir genC/
else 
	rm genC/*
fi

for teste in "${testes[@]}"
do
	# Pega o nome do teste para colocar como arquivo de saida
	outfile=($(cut -d'/' -f3 <<<$teste | cut -d'.' -f1))

	java -classpath src/ Main $teste genC/$outfile.c

done

cfiles=($(ls -v genC))

for cfile in "${cfiles[@]}"
do
	# if files exists and has size greater than zero
	if [ -s genC/$cfile ]; then
		obj=($(cut -d'.' -f1 <<<$cfile))
		printf "\n%s\n" "$obj"
		gcc -o genC/$obj genC/$cfile

		if [ -s genC/$obj ]; then
			genC/$obj
		fi
	fi
done

