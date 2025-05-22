.DEFAULT_GOAL := build-run

build:
	make -C app build

test:
	make -C app test

report:
	make -C app report

.PHONY: build test report
