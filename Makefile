# compile java protobuf
protoc-gen-all:
	docker run -v ${PWD}:/defs namely/protoc-all -d src/main/proto -l java -o ./src/main/java