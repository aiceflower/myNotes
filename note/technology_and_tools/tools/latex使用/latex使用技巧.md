1.常用包

\usepackage{amsmath}   : 矩阵

\usepackage{graphicx} : 画图

2.空格

\quad \qquad \hspace{长度单位}

换页：\newspace

换行后空格无效问题: \indent

换行顶格、不缩进: \noindent



长度单位

|单位名称|	大小|
|-|-|
|pt	|point点，欧美传统排版单位 ，亦称磅|
|pc	|pica，1 pc = 12 pt， 相当于四号字大小|
|dd	|didot point，欧洲大陆使用，1157 dd = 1238 pt|
|cc	|cicero，欧洲大陆使用，1 cc = 12 dd|
|in|	inch，英寸，1 in = 72.27pt|
|bp|	big point，1 in = 72 bp|
|cm|	厘米，1 in = 2.54 cm|
|mm|	毫米|
|sp	|scaled point， 最小的长度单位，1 pt = 65536 sp|
|em|	与字号有关，相当于M的宽度，亦等于\quad的长度|
|ex|	与字号和字体有关，相当于x的高度|

3.编译标题

```latex
\title{周报-20200608}
\author{郝金福}
\date{2020-06-08}
\begin{document}
	\maketitle
\end{document}
```

4.条目

```latex
\begin{itemize}
		\item 1
		\item 2
\end{itemize}
```

5.图片

```latex
\begin{figure}[h]
	\centering %居中
	\includegraphics[scale=0.35{/data/4.png}
\end{figure}
```

