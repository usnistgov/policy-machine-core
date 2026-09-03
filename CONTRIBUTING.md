# Contributing

Thank you for taking an interest in contributing. This documentation contains some general guidelines for contributions to this project. These guidelines describe and govern NIST’s management of this repository and contributors’ responsibilities. NIST reserves the right to modify this policy at any time.

## Criteria for Contributions and Feedback

This is a moderated platform. NIST will only accept contributions that are contributed per the terms of the license file. All contributors must add their name to the [CONTRIBUTORS.md](./CONTRIBUTORS.md) file to affirmatively indicate acceptance of the license terms. Contributions cannot be accepted from user accounts for which this acceptance has not been done. Upon submission, materials will be public and considered publicly available information, unless noted in the license file.

NIST reserves the right to reject, remove, or edit any contribution or feedback, including anything that:

-   states or implies NIST endorsement of any entities, services, or products;

-   is inaccurate;

-   contains abusive or vulgar content, spam, hate speech, personal attacks, or similar content;

-   is clearly "off topic";

-   makes unsupported accusations;

-   includes personally identifiable or business identifiable information according to Department of Commerce Office of Privacy and Open Government [guidelines](https://www.commerce.gov/sites/default/files/opog/DOC_PA_PII_and_BII_Breach_Notification_Plan.pdf)

## Contributor Responsibilities

NIST also reserves the right to reject or remove contributions from the repository if the contributor fails to carry out any of the following responsibilities:

-   following the contribution instructions;

-   responding to feedback from other repository users in a timely manner;

-   responding to NIST representatives in a timely manner;

-   keeping contributions and contributor GitHub username up to date

## Question or Problem?

If you have any questions or problems with the software please open a [discussion or issue](https://github.com/PM-Master/policy-machine-core/issues) on the repository.

## Submitting Issues

Prior to submitting any issue please try a brief search to see if an issue similar to yours already exists. Avoiding duplicate submissions helps to limit time needed to triage issues. Each issue card should be focused and describe a single bug, feature, etc.

### Bugs

If you find a bug or unexpected behavior please open an [issue](https://github.com/PM-Master/policy-machine-core/issues/new) and describe the issue along with step-by-step instructions on how to reproduce it.

### Feature Requests

Please feel free to suggest new features or capabilities. Try to describe what the desired feature will do and how it will behave and try to avoid implementation details.

## Code Submission Guidelines

This and following sections describe how to participate in the development process if you desire to contribute.

Create an issue first to spec out the change and requirements before starting work. A pull request should be tied to at least one issue (exceptions are **chore** changes like updating copyright year, files to ignore, etc.). Keep the number of issues covered in a pull request minimal. Multiple issues being resolved in a single pull request should only happen in exceptional circumstances.

### Development Workflow

[Clone](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository) this repository.

Create a [new branch](https://git-scm.com/book/en/v2/Git-Branching-Basic-Branching-and-Merging) off the `main` branch to get started.

This project uses git submodules (`grpc/protos`), so clone with `--recurse-submodules` or run `git submodule update --init --recursive` after cloning.

#### Squashing

All final commits will be squashed, therefore when squashing your branch, it’s important to make sure you update the commit message. If you’re using GitHub’s UI it will by default create a new commit message which is a combination of all commits and **does not follow the commit guidelines**.

If you’re working locally, it often can be useful to `--amend` a commit, or utilize `rebase -i` to reorder, squash, and reword your commits.

### Testing

All new features and submissions require tests. Run the full test suite with:

```
mvn test
```
