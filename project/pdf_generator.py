#!/usr/bin/env python

import random
from jinja2 import Environment, FileSystemLoader
import os
import subprocess
import tempfile
import shutil

dirs = ['NodeA', 'NodeB', 'NodeC', 'NodeD']

titles = ["Lorem ipsum", "Some title", "Some other title", "A very promising title",
          "A fancy title"]
keywords = ["big data", "YARN", "Hadoop", "Hops", "Hops-YARN", "Hops-Hadoop", "Distributed Systems",
            "Cloud", "IoT", "Security", "HDFS", "Hops-HDFS", "Constraint programming"]
authors = ["Antonis Kouzoupis", "Alice Bar", "Bob Foo", "Joe Smith"]
    
def gen(template, index, dir, tmp, cwd):
    title_rnd = random.randrange(len(titles))
    author_rnd = random.randrange(len(authors))

    title = titles[title_rnd]
    author = authors[author_rnd]
    keys = random.sample(keywords, 3)
    keys_str = ', '.join(keys)

    rendered = template.render(author=author, keywords=keys_str, title=title)

    tex_file = os.path.join(tmp, "another_file%i.tex" % (index))
    pdf_file_src = os.path.join(tmp, "another_file%i.pdf" % (index))
    pdf_file_dst = os.path.join(cwd, dir, "another_file%i.pdf" % (index))
    
    with open(tex_file, 'w') as f:
        f.write(rendered)

    subprocess.call(['xelatex', tex_file])

    shutil.move(pdf_file_src, pdf_file_dst)

def checkNCreate(dir):
    if (not os.path.exists(dir)):
        os.mkdir(dir)
        
if __name__ == "__main__":
    env = Environment(loader=FileSystemLoader('templates'))
    template = env.get_template('template.tex')
    cwd = os.getcwd()
    tmp = tempfile.mkdtemp()
    os.chdir(tmp)
    for dir in dirs:
        checkNCreate(os.path.join(cwd, dir))
        # Make also one subdirectory
        checkNCreate(os.path.join(cwd, dir, 'subdir'))

        for index in range(0, 5):
            gen(template, index, dir, tmp, cwd)
            gen(template, index, os.path.join(dir, 'subdir'), tmp, cwd)
        
    shutil.rmtree(tmp)
