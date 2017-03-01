all: data_model.pdf sharing.pdf

%.pdf: %.tex
	pdflatex -halt-on-error $<

clean:
	rm -f *.pdf *.aux *.log
