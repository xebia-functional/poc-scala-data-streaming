<h1 id="diamond-architecture-motivation">Diamond Architecture Motivation</h1>

The diamond architecture is based on a set of rules that allow a great degree of flexibility, making it a good choice
for project that have to change over time.

<h2 id="rules">Rules</h2>

The rules of the diamond architecture are:

- Have a clear unidirectional dependency graph
- Keep the dependencies of each module to the minimum
- Every layer of modules depends on the prior layer
- Dependencies across the same layer should be avoided

<h2 id="module-naming-convention">Module Naming Convention</h2>

On top of the rules, there are some recommendations about how to name each module. For example, the modules in this 
project follow the diamond architecture nomenclature: `<layer>-<type>-<function>-<library>`

<h3 id="logical-layers">Logical Layers</h3>

There can be as many layers as needed, but the recommendation is not less than three and not more than six. There are
four layers in this project.

| Layer                                 | Responsibility                               | 
|---------------------------------------|----------------------------------------------|
| <p style="text-align: center;">01</p> | API/Interfaces                               |
| <p style="text-align: center;">02</p> | Common elements                              |
| <p style="text-align: center;">03</p> | Implementations                              |
| <p style="text-align: center;">04</p> | Composition of interfaces to run the project |

<h3 id="types-of-modules">Types of Modules</h3>

There can be as many types of modules as needed, but with the following four, most of the cases should be covered.

| Type                                 | Description                                                                    | 
|--------------------------------------|--------------------------------------------------------------------------------|
| <p style="text-align: center;">c</p> | Common modules that share functionality across the project                     |
| <p style="text-align: center;">i</p> | Input modules that bring data into the project's pipeline                      |
| <p style="text-align: center;">o</p> | Output modules that process the data and send it somewhere outside the project |
| <p style="text-align: center;">u</p> | Util modules that help modules that share the same underlying library          |

<h3 id="functions-of-modules">Functions of Modules</h3>

The function of the modules will vary based on the project's needs. In this PoC we used the following

| Type                                             | Description                                                               | 
|--------------------------------------------------|---------------------------------------------------------------------------|
| <p style="text-align: center;">configuration</p> | Load the project's configuration                                          |
| <p style="text-align: center;">consumer</p>      | Consumes the data from upstream sources                                   |
| <p style="text-align: center;">core</p>          | Holds the business model and logic                                        |
| <p style="text-align: center;">dataGenerator</p> | Generates dummy data (replaces upstream sources)                          |
| <p style="text-align: center;">processor</p>     | Applies the business logic to the data and sends it to the external sinks |

<h3 id="libraries-of-modules">Libraries of Modules</h3>

Finally, the last tag, libraries, refers to the main library of the given module (when applicable).

For example, the configuration module is simply named as `02-c-config` and the flink processor is
`03-o-processor-flink` because we have multiple libraries used for processing.
