# FAIR Software & Data

The following material is paraphrased from the NIST-internal
[Data Sponsorship][sponsors] repository by @tkphd.

## Table of Contents

- [FAIR principles](#fair-principles)
  - [Findable](#findable)
  - [Accessible](#accessible)
  - [Interoperable](#interoperable)
  - [Reusable](#reusable)
- [Make It FAIR in Ten Easy Steps](#make-it-fair-in-ten-easy-steps)
- [What's "good enough"?](#whats-good-enough)
  - [Data management](#data-management)
  - [Software](#software)
  - [Collaboration](#collaboration)
  - [Project organization](#project-organization)
  - [Keeping track of changes](#keeping-track-of-changes)
  - [Manuscripts](#manuscripts)
- [Links](#links)

## FAIR principles

What does FAIR even mean? The following sections reproduce the
summary from [Go FAIR][gofair], based on the original
[FAIR paper][fair].

Think you know FAIR? Please [use this tool][fair-aware] to check your
awareness!

### Findable

The first step in (re)using data is to find them. Metadata and data
should be easy to find for both humans and computers.
Machine-readable metadata are essential for automatic discovery of
datasets and services, so this is an essential component of the
FAIRification process.

1. (Meta)data are assigned a globally unique and persistent
   identifier
2. Data are described with rich metadata (defined by R1 below)
3. Metadata clearly and explicitly include the identifier of the data
   they describe
4. (Meta)data are registered or indexed in a searchable resource

### Accessible

Once the user finds the required data, she/he/they need to know how
can they be accessed, possibly including authentication and
authorisation.

1. (Meta)data are retrievable by their identifier using a
   standardised communications protocol
   1. The protocol is open, free, and universally implementable
   2. The protocol allows for an authentication and authorisation
      procedure, where necessary
2. Metadata are accessible, even when the data are no longer
   available

### Interoperable

The data usually need to be integrated with other data. In addition,
the data need to interoperate with applications or workflows for
analysis, storage, and processing.

1. (Meta)data use a formal, accessible, shared, and broadly
   applicable language for knowledge representation.
2. (Meta)data use vocabularies that follow FAIR principles
3. (Meta)data include qualified references to other (meta)data

### Reusable

The ultimate goal of FAIR is to optimise the reuse of data. To
achieve this, metadata and data should be well-described so that they
can be replicated and/or combined in different settings.

1. (Meta)data are richly described with a plurality of accurate and
   relevant attributes
   1. (Meta)data are released with a clear and accessible data usage
      license
   2. (Meta)data are associated with detailed provenance
   3. (Meta)data meet domain-relevant community standards

## Make It FAIR in Ten Easy Steps

[Library Carpentry][lc] has a summary of [10 "easy" steps to make your
software FAIR][lc-fair-poster] (PDF). An annotated list follows.
Note that while the list is software-centric, it applies equally to data.

1. *Create a description of your software.*  
   Write this in `README.md` with supporting tables, charts, images,
   etc. Include its dependencies, installation instructions, and
   citations of any work it builds upon.
2. *Register your software in a software registry.*  
   [MIDAS][midas] is the go-to where Data Sponsorship is concerned,
   but is not the only option.
3. *Use a unique and persistent identifier for your software.*  
   Any registry compliant with [NIST O 5702][o5702] will provide you
   with a persistent handle.
4. *Make sure that people can download your software.*  
   If the data is fire- or pay-walled, provide an alternative site.
   Wherever your data lives, check back from time to time to make
   sure the links are still valid.
5. *Explain the functionality of your software.*  
   Write this into a "Usage" section of `README.md`, or similar,
   with example of how to configure, launch, and interact with the
   software, with examples of output to be expected.
6. *Use standard (community-agreed) formats for inputs and outputs.*  
   While open standards are preferred, if a proprietary format is the
   *lingua franca* of the field, focus on that. Create open versions
   if possible.
7. *Document your software.*  
    This goes beyond `README.md` and in-line comments. Place
    documentation, or its build scripts, in a folder named `doc` with
    its own `README.md` describing how to build the docs and what to
    expect.
8. *Give your software a license.*  
   If all members of the development team are Federal employees, use
   the standard NIST Disclaimer of Copyright and Warranty for your
   [LICENSE.md][disc-copy]. Otherwise, decide on an appropriate
   license.
9. *State how to cite your software.*  
   This can be done in `README.md`, or as a separate
   [`CITATION.md`][citation] using a BiBTeX-styled code block.
10. *Follow best practices for software development.*  
   Broadly speaking, this starts with version control using
   [git][git] or similar, [linting][lint] your code, and following
   some type of branching workflow when multiple developers are
   involved. The regularly-scheduled Software Carpentry workshops at
   NIST teach the basics of some of these concepts.

## What's good enough?

[*Good enough practices in scientific computing*][gepsc] is an
excellent paper outlining what you need to do to produce good science
in a FAIR frame of mind.

*tl;dr* follows.

### Data management

1. Save the raw data.
2. Ensure that raw data are backed up in more than one location.
3. Create the data you wish to see in the world.
4. Create analysis-friendly data.
5. Record all the steps used to process data.
6. Anticipate the need to use multiple tables, and use a unique
   identifier for every record.
7. Submit data to a reputable DOI-issuing repository so that others
   can access and cite it.

### Software

1. Place a brief explanatory comment at the start of every program.
2. Decompose programs into functions.
3. Be ruthless about eliminating duplication.
4. Always search for well-maintained software libraries that do what
   you need.
5. Test libraries before relying on them.
6. Give functions and variables meaningful names.
7. Make dependencies and requirements explicit.
8. Do not comment and uncomment sections of code to control a
   program's behavior.
9. Provide a simple example or test data set.
10. Submit code to a reputable DOI-issuing repository.

### Collaboration

1. Create an overview of your project.
2. Create a shared "to-do" list for the project.
3. Decide on communication strategies.
4. Make the license explicit.
5. Make the project citable.

### Project organization

1. Put each project in its own directory, which is named after the
   project.
2. Put text documents associated with the project in the `doc`
   directory.
3. Put raw data and metadata in a `data` directory and files generated
   during cleanup and analysis in a results directory.
4. Put project source code in the `src` directory.
5. Put external scripts or compiled programs in the `bin` directory.
6. Name all files to reflect their content or function.

### Keeping track of changes

1. Back up (almost) everything created by a human being as soon as it
   is created.
2. Keep changes small.
3. Share changes frequently.
4. Create, maintain, and use a checklist for saving and sharing
   changes to the project.
5. Store each project in a folder that is mirrored off the
   researcher's working machine.
6. Add a file called `CHANGELOG.md` to the project's docs subfolder.
7. Copy the entire project whenever a significant change has been
   made.
8. Use a version control system.

### Manuscripts

1. Write manuscripts using online tools with rich formatting, change
   tracking, and reference management.
2. Write the manuscript in a plain text format that permits version
   control.

## Links

- [F-UJI][fuji] (*tool*): analyze a repository and get a report of
  its FAIR compliance, with an overall score and a checklist
- [FAIRaware][fair-aware] quiz/checklist of understanding
- [FAIR for Research Software (FAIR4RS)][fair4rs], a proposed
  modification of the FAIR principles specifically for software.
- [10 easy things to make your software FAIR!][lc-fair-poster] (PDF)
  from [Library Carpentry][lc-fair]
- [4 Simple Recommendations for Open-Source Software][4oss]
  walk-through lesson, Carpentries-style

Some more general FAIR resources:

- Research Data Alliance: [Top 10 FAIR data and software things](https://www.rd-alliance.org/group/libraries-research-data-ig/outcomes/top-10-fair-data-software-things)
- Library Carpentries: [Top 10 FAIR Research Software Things](https://librarycarpentry.org/Top-10-FAIR//2018/12/01/research-software/)
- [NLeSC FAIR Software](https://fair-software.eu/)
- [NLeSC howfairis](https://github.com/fair-software/howfairis)
- [Data and Software Sharing Guidance for Authors Submitting to AGU journals](https://data.agu.org/resources/agu-data-software-sharing-guidance)
- [FAIRSharing and FAIRShake](https://presqt.readthedocs.io/en/latest/)
  from the Preservation Quality Tool (PresQT)
- [Registry of Research Data Repositories](https://re3data.org/)
  also has software locations
- [*Automating the Monitoring of Research Software FAIR Metrics*](https://doi.org/10.7490/f1000research.1117992.1)
- [Proposal for software indicators in the Open Science Monitor](https://www.ouvrirlascience.fr/about-the-proposal-for-software-indicators-in-open-science-monitor-3/)
- *National Plan for Open Science* (France, 2021-2024).
  [Theme Three: Opening Up and Promoting Source Code Produced by Research](https://www.ouvrirlascience.fr/second-national-plan-for-open-science/)
- [The Turing Way](https://the-turing-way.netlify.app/welcome)
- [*Software Citation Guide*](https://doi.org/10.12688/f1000research.26932.2)
- [Managing Research Software Projects](https://swcarpentry.github.io/managing-research-software-projects/)
- [Guides](https://www.software.ac.uk/resources/guides)
  for several groups (researchers, managers, developers, ...) from
  the Software Sustainability Institute
- [Chorus Software Citation Policies Index](https://chorusaccess.org)
- [Software Discovery Through Registries](https://softdev4research.github.io/4OSS-lesson/05-use-registry/index.html)
- [Awesome FAIR Data][afd]: a list of FAIR data resources.
- [Awesome Research Software Registries](https://github.com/NLeSC/awesome-research-software-registries/blob/main/README.md)
- [CodeMeta Standard](https://codemeta.github.io/)
  - [CodeMeta Generator][cmg] (*tool*): generate a complete set of
    [CodeMeta][codemeta]-compliant metadata for your research software
    and/or data with this handy form. Exports to JSON.  
    *Note:* `codemeta.json` is *not* the same as `codemeta.yaml`: the
    former is a nascent general schema, while the latter is only used
    to help index and link NIST websites.
- [SOftware Metadata Extraction Framework (SOMEF)](https://github.com/KnowledgeCaptureAndDiscovery/somef/)
- [*Good Enough Practices in Scientific Computing*](https://doi.org/10.1371/journal.pcbi.1005510)
- [Citation File Format][citation] docs from GitHub
- [Software REUSE Specification](https://reuse.software/spec/)
- [FAIR Computational Workflows][fcw] (*paper*): Data doesn't just
  happen. Record the workflow that created it to be super FAIR.

Institutional guidance:

- DLR: [Software Engineering Initiative](https://rse.dlr.de/01_guidelines.html)
- MIT: [Software Citation and Publishing](https://libguides.mit.edu/software)
- MIT workshop: [Managing your research code](https://www.dropbox.com/s/gukkthqzcuea1kr/MgingCode_Slides_MIT.pdf?dl=0)
- TU Delft [*Guidelines on Research Software: Licensing, Registration and Commercialisation*](https://doi.org/10.5281/zenodo.4629635)
- TU Delft: [Choosing a Repository Manager](https://doi.org/10.5281/zenodo.4710206)
- Helmholtz: [Guidelines for Sustainable Research Software](https://gfzpublic.gfz-potsdam.de/pubman/faces/ViewItemOverviewPage.jsp?itemId=item_4906899)
  - [Checklist for Helmholtz Guidelines](https://gfzpublic.gfz-potsdam.de/pubman/faces/ViewItemOverviewPage.jsp?itemId=item_5007561)
- NIH: [Best Practices for Sharing Research Software](https://datascience.nih.gov/tools-and-analytics/best-practices-for-sharing-research-software-faq)

<!-- links -->

[4oss]: https://softdev4research.github.io/4OSS-lesson/
[afd]: https://github.com/Materials-Data-Science-and-Informatics/awesome-fair-data
[citation]: https://github.com/citation-file-format/citation-file-format
[cmg]: https://codemeta.github.io/codemeta-generator/
[codemeta]: https://codemeta.github.io/
[disc-copy]: https://www.nist.gov/open/copyright-fair-use-and-licensing-statements-srd-data-software-and-technical-series-publications
[fair]: https://doi.org/10.1038/sdata.2016.18
[fair4rs]: https://doi.org/10.1016/j.patter.2021.100222
[fair-aware]: https://fairaware.dans.knaw.nl
[fcw]: https://doi.org/10.1162/dint_a_00033
[fuji]: https://www.f-uji.net/index.php?action=home
[gepsc]: https://doi.org/10.1371/journal.pcbi.1005510
[git]: https://git-scm.com
[git-open]: https://github.com/usnistgov/opensource-repo
[gofair]: https://www.go-fair.org/fair-principles/
[lc-fair]: https://librarycarpentry.org/Top-10-FAIR//2018/12/01/research-software/
[lc-fair-poster]: https://librarycarpentry.org/Top-10-FAIR/files/poster_10things_FAIRsoftware.pdf
[lint]: https://en.wikipedia.org/wiki/Lint_(software)
[midas]: https://midas.nist.gov/
[o5701]: https://inet.nist.gov/adlp/directives/managing-public-access-results-federally-funded-research-0
[o5702]: https://inet.nist.gov/adlp/directives/preservation-maintenance-published-research-data
[sp811]: https://dx.doi.org/10.6028/NIST.SP.811e2008
[sponsors]: https://gitlab.nist.gov/gitlab/tkphd/data-sponsorship
[taxon]: https://doi.org/10.18434/T4/1432795
