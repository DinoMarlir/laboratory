import React from 'react';
import clsx from 'clsx';
import styles from './HomepageFeatures.module.css';

const FeatureList = [
  {
      title: 'Easy to Use',
      Svg: require('../../static/img/easy-to-use.svg').default,
      description: (
          <>
              Laboratory is designed to be very user-friendly but powerful at the same time.
          </>
    ),
  },
  {
    title: 'Focus on What Matters',
    Svg: require('../../static/img/undraw_docusaurus_tree.svg').default,
    description: (
      <>
        You can focus on your work and we&apos;ll take care of the annoying.
        Your server will be automatically updated, you can also setup tasks that will be scheduled.
      </>
    ),
  },
  {
    title: 'Powered by Kotlin',
    Svg: require('../../static/img/undraw_docusaurus_react.svg').default,
    description: (
      <>
        Laboratory is built with Kotlin, meaning that it will run on almost every system
      </>
    ),
  },
];

function Feature({Svg, title, description}) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <Svg className={styles.featureSvg} alt={title}/>
      </div>
      <div className="text--center padding-horiz--md">
        <h3>{title}</h3>
        <p>{description}</p>
      </div>
    </div>
  );
}


export default function HomepageFeatures() {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
